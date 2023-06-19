package ae.phonecheckers.phone;

import java.util.ArrayList;
import java.util.List;

import ae.phonecheckers.fono.api.model.PhoneSpec;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
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
    public String model;
    public String extRef;
    public String technology;
    public String g2Bands;
    public String g3Bands;
    public String g4Bands;

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
        newPhone.getInventory().add(Inventory.init(newPhone));

        return newPhone;
    }
}
