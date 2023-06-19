package ae.phonecheckers.fono.api;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import ae.phonecheckers.fono.api.model.PhoneSpec;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FonoUtil {

        List<String> phones = List.of("Samsung Galaxy S9", "Samsung Galaxy S8", "Motorola Nexus 6", "Oneplus 9",
                        "Apple iPhone 13", "Apple iPhone 12", "Apple iPhone 11", "iPhone X", "Nokia 3310");

        Map<String, PhoneSpec> initStore() {
                return phones.stream()
                                .map(phoneName -> new PhoneSpec(phoneName, generateIdentifier.apply(phoneName),
                                                TECH.get(random.nextInt(3)), G2.get(random.nextInt(3)),
                                                G3.get(random.nextInt(3)),
                                                G4.get(random.nextInt(3))))
                                .collect(Collectors.toMap(spec -> spec.modelIdentifier(), Function.identity()));
        }

        private Random random = new Random();

        PhoneSpec getDefaultSpec(String deviceName) {
                return new PhoneSpec(deviceName, null, null, null, null, null);
        }

        private Function<String, String> generateIdentifier = (phoneName) -> phoneName.replaceAll("\\s", "")
                        .toLowerCase();

        private final List<String> TECH = List.of(
                        "GSM / CDMA / HSPA / EVDO / LTE",
                        "GSM / HSPA / LTE",
                        "GSM / CDMA / HSPA / LTE");

        private final List<String> G2 = List.of(
                        "GSM 850 / 900 / 1800 / 1900 - SIM 1 & SIM 2 (dual-SIM model only)",
                        "GSM 850 / 900 / 1800 / 1900 - SIM 1 & SIM 2 (dual-SIM model only)",
                        "GSM 850 / 900");

        private final List<String> G3 = List.of(
                        "HSDPA 850 / 900 / 1700(AWS) / 1900 / 2100",
                        "HSDPA 850 / 900 / 1700(AWS) / 1900 / 2100",
                        "HSDPA 800 / 850 / 900 / 1700(AWS) / 1800 / 1900 / 2100");

        private final List<String> G4 = List.of(
                        "LTE band 1(2000), 2(1800), 3(1700), 4(1700/1800), 5(750), 7(2500), 8(900), 17(700), 20(800), 28(700)",
                        "LTE band 1(2400), 2(1300), 3(1800), 4(1700/2100), 5(850), 7(2600), 8(900), 9(1800), 17(700), 19(800), 20(800)",
                        "LTE band 1(2100), 2(1900), 3(1800), 4(1700/2100), 5(850), 7(2600), 8(900), 17(700), 20(800), 28(700)");
}
