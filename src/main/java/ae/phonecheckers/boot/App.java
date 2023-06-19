package ae.phonecheckers.boot;

import io.quarkus.runtime.Quarkus;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
class App {

    public static void main(String... args) {
        Quarkus.run(args);
    }
}