package ae.phonecheckers.boot;

import java.util.List;
import java.util.function.Function;

import org.jboss.logging.Logger;

import ae.phonecheckers.fono.api.FonoApi;
import ae.phonecheckers.fono.api.model.PhoneSpec;
import ae.phonecheckers.phone.Inventory;
import ae.phonecheckers.phone.Phone;
import ae.phonecheckers.phone.PhoneRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DataLoader {

    @Inject
    FonoApi fonoApi;

    @Inject
    PhoneRepository phoneRepository;

    private static final Logger LOG = Logger.getLogger("DataLoader");

    @Transactional
    // void onStart(@Observes StartupEvent ev, Mutiny.SessionFactory sf) {
    void onStart(@Observes StartupEvent ev) {

        LOG.info("Preparing Fono API");
        List<String> phones = List.of("Samsung Galaxy S9", "Samsung Galaxy S8",
                "Motorola Nexus 6", "Oneplus 9", "Samsung Galaxy S8",
                "Apple iPhone 13", "Apple iPhone 12", "Apple iPhone 11", "iPhone X", "Nokia 3310");

        Multi.createFrom().iterable(phones)
                // .emitOn(Infrastructure.getDefaultWorkerPool())
                .map(getIdentifier)
                .map(fonoApi::getPhoneSpec)
                .map(registerPhone)
                .subscribe()
                .with(
                        phone -> LOG.infof("Phone persisted %s", phone.model),
                        err -> LOG.errorf(err, "Unable to finish registering phones. %s", err.getMessage()),
                        () -> printDataLoadSummary());
        LOG.info("Phone data store updated");
        // Quarkus.waitForExit();
    }

    private void printDataLoadSummary() {
        PanacheQuery<Phone> findAll = phoneRepository.findAll();
        LOG.infof("%n%nTotal phones saved: %s. %nTotal Inventory size: %s%n",
                findAll.count(),
                findAll.stream()
                        .mapToInt(phone -> phone.inventory.size())
                        .sum());
    }

    private Function<String, String> getIdentifier = (phoneName) -> phoneName.replaceAll("\\s", "").toLowerCase();

    private Function<PhoneSpec, Phone> registerPhone = (spec) -> phoneRepository.findByModel(spec.modelName())
            .map(phone -> updateInventory(phone))
            .orElseGet(() -> addPhoneAndUpdateInventory(spec));

    // private Uni<Phone> registerPhone1(PhoneSpec spec) {
    // return phoneRepository.findByModel(spec.modelName())
    // // .map(phone -> updateInventory(phone))
    // // .orElseGet(() -> addPhoneAndUpdateInventory(spec));
    // // If phone model already existing in inventory, update item count
    // .onItem().ifNotNull().transform(this::updateInventory)
    // // Else add new phone to inventory
    // .onItem().ifNull().continueWith(() -> addPhoneAndUpdateInventory(spec));

    // }

    Phone addPhoneAndUpdateInventory(PhoneSpec spec) {
        Phone newPhone = Phone.init(spec);
        phoneRepository.persistAndFlush(newPhone);
        return newPhone;
    }

    Phone updateInventory(Phone phone) {
        phone.inventory.add(Inventory.init(phone));
        phoneRepository.persistAndFlush(phone);
        return phone;
    }

    // public Uni<Phone> addPhone(PhoneSpec phoneDetails) {

    // return phoneRepository.findByModel(phoneDetails.modelName())
    // // If phone model already existing in inventory, update item count
    // .onItem().ifNotNull().transform(this::updateExistingPhoneToInventory)
    // // Else add new phone to inventory
    // .onItem().ifNull().continueWith(() -> addNewPhoneToInventory(phoneDetails))
    // .await().indefinitely();
    // }

    // private Phone addNewPhoneToInventory(PhoneSpec newPhoneDetails) {
    // Phone newPhone = Phone.init(newPhoneDetails);
    // newPhone.persistAndFlush();
    // return newPhone;
    // }

    // private Uni<Phone> updateExistingPhoneToInventory(Phone existingPhone) {
    // existingPhone.setItemCount(existingPhone.getItemCount() + 1);
    // return existingPhone.persistAndFlush();
    // }

}
