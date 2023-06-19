package ae.phonecheckers.phone;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Setter;

@Entity
@Setter
public class Inventory extends PanacheEntity {

    @ManyToOne
    public Phone phone;

    @OneToOne(mappedBy = "inventory")
    public Booking booking;

    public static Inventory init(Phone newPhone) {
        Inventory newInventory = new Inventory();
        newInventory.setPhone(newPhone);
        return newInventory;
    }

    public boolean isAvailable() {
        return booking == null;
    }

    public boolean isBooked() {
        return booking != null;
    }
}