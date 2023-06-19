package ae.phonecheckers.phone;

import java.util.Optional;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PhoneRepository implements PanacheRepository<Phone> {

    // public Uni<Phone> findByModel(String model) {
    // return find("model", model).firstResult();
    // }

    public Optional<Phone> findByModel(String model) {
        return find("model", model).firstResultOptional();
    }

    public Optional<Phone> findByExtRef(String identifier) {
        return find("extRef", identifier).firstResultOptional();
    }

    // public List<Phone> findAll() {
    // return listAll();
    // }
}
