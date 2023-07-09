package ae.phonecheckers.boot;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.LongStream;

import ae.phonecheckers.fono.api.FonoApi;
import ae.phonecheckers.fono.api.model.PhoneSpec;
import ae.phonecheckers.phone.Inventory;
import ae.phonecheckers.phone.Phone;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DataLoader {

	@Inject
	private FonoApi fonoApi;

	private Map<String, Long> phones = List.of(
			"Samsung Galaxy S9", "Samsung Galaxy S8", "Motorola Nexus 6",
			"Oneplus 9", "Samsung Galaxy S8", "Apple iPhone 13",
			"Apple iPhone 12", "Apple iPhone 11", "iPhone X", "Nokia 3310")
			.stream()
			.collect(groupingBy(Function.identity(), counting()));

	@Transactional
	void onStart(@Observes StartupEvent ev) {

		Log.infov("Phone data store prep: {0}", phones);

		Multi.createFrom().iterable(phones.keySet())
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
		PanacheQuery<Phone> findAll = Phone.findAll();
		Log.infof("%n%nTotal phones saved: %s. %nTotal Inventory size: %s%n",
				findAll.count(),
				findAll.stream()
						.mapToInt(phone -> phone.inventory.size())
						.sum());
	}

	private Function<String, String> getIdentifier = (phoneName) -> phoneName.replaceAll("\\s", "").toLowerCase();

	private Function<String, Long> getInventoryCountForModel = (modelName) -> phones.get(modelName);

	private Function<PhoneSpec, Phone> registerPhone = (spec) -> {
		Phone newPhone = Phone.init(spec);
		LongStream.range(0, getInventoryCountForModel.apply(spec.modelName()))
				.forEach(_ignore -> newPhone.inventory.add(Inventory.init(newPhone)));
		newPhone.persistAndFlush();
		return newPhone;
	};
}
