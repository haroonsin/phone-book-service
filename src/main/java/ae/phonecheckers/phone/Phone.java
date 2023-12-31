package ae.phonecheckers.phone;

import java.util.ArrayList;
import java.util.List;

import ae.phonecheckers.fono.api.model.PhoneSpec;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "phone")
public class Phone extends PanacheEntity {
    private String model;
    private String extRef;
    private String technology;
    private String g2Bands;
    private String g3Bands;
    private String g4Bands;

    @OneToMany(mappedBy = "phone", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    public final List<Inventory> inventory = new ArrayList<>();

    public static Phone init(PhoneSpec spec) {
        Phone newPhone = new Phone();
        newPhone.setExtRef(spec.modelIdentifier());
        newPhone.setModel(spec.modelName());
        newPhone.setG2Bands(spec.g2Bands());
        newPhone.setG3Bands(spec.g3Bands());
        newPhone.setG4Bands(spec.g4Bands());
        newPhone.setTechnology(spec.technology());

        return newPhone;
    }
}
