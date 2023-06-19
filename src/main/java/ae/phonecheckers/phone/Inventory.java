package ae.phonecheckers.phone;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Inventory extends PanacheEntity {
    @ManyToOne
    public Phone phone;
    public byte available;

    public static Inventory init() {
        return null;
    }

    public static Inventory init(Phone newPhone) {
        Inventory newInventory = new Inventory();
        newInventory.phone = newPhone;
        newInventory.available = 1;
        return newInventory;
    }
}