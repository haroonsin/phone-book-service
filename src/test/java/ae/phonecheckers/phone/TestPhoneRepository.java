package ae.phonecheckers.phone;

import java.util.List;
import java.util.function.Function;

import ae.phonecheckers.fono.api.FonoApi;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;

// @Priority(1)
// @Alternative
// @ApplicationScoped
public class TestPhoneRepository extends PhoneRepository {

    // @Inject
    // FonoApi fonoApi;

    // @PostConstruct
    // public void init() {
    // List<String> phones = List.of("Samsung Galaxy S9", "Samsung Galaxy S8",
    // "Motorola Nexus 6", "Oneplus 9", "Samsung Galaxy S8",
    // "Apple iPhone 13", "Apple iPhone 12", "Apple iPhone 11", "iPhone X", "Nokia
    // 3310");

    // phones.stream()
    // .map(phoneName -> fonoApi.getPhoneSpec(getIdentifier.apply(phoneName)))
    // .map(spec -> Phone.init(spec))
    // .forEach(phone -> persist(phone));
    // }

    // private Function<String, String> getIdentifier = (phoneName) ->
    // phoneName.replaceAll("\\s", "").toLowerCase();

}