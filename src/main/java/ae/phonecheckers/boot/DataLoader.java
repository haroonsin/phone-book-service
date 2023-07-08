package ae.phonecheckers.boot;

import java.util.List;
import java.util.function.Function;

import ae.phonecheckers.fono.api.FonoApi;
import ae.phonecheckers.fono.api.model.PhoneSpec;
import ae.phonecheckers.phone.Inventory;
import ae.phonecheckers.phone.Phone;
import ae.phonecheckers.phone.PhoneRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DataLoader {

    private FonoApi fonoApi;
    private PhoneRepository phoneRepository;

    public DataLoader(FonoApi fonoApi, PhoneRepository phoneRepository) {
        this.fonoApi = fonoApi;
        this.phoneRepository = phoneRepository;
    }

    @Transactional
    void onStart(@Observes StartupEvent ev) {

        Log.info("Preparing Fono API");
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
                        phone -> Log.infof("Phone persisted %s", phone.getModel()),
                        err -> Log.errorf(err, "Unable to finish registering phones. %s", err.getMessage()),
                        () -> printDataLoadSummary());
        Log.info("Phone data store updated");
    }

    private void printDataLoadSummary() {
        PanacheQuery<Phone> findAll = phoneRepository.findAll();
        Log.infof("%n%nTotal phones saved: %s. %nTotal Inventory size: %s%n",
                findAll.count(),
                findAll.stream()
                        .mapToInt(phone -> phone.inventory.size())
                        .sum());
    }

    private Function<String, String> getIdentifier = (phoneName) -> phoneName.replaceAll("\\s", "").toLowerCase();

    private Function<PhoneSpec, Phone> registerPhone = (spec) -> phoneRepository.findByModel(spec.modelName())
            .map(phone -> updateInventory(phone))
            .orElseGet(() -> addPhoneAndUpdateInventory(spec));

    private Phone addPhoneAndUpdateInventory(PhoneSpec spec) {
        Phone newPhone = Phone.init(spec);
        phoneRepository.persistAndFlush(newPhone);
        return newPhone;
    }

    private Phone updateInventory(Phone phone) {
        phone.inventory.add(Inventory.init(phone));
        phoneRepository.persistAndFlush(phone);
        return phone;
    }
}
