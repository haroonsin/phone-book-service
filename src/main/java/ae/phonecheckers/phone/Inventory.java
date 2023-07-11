package ae.phonecheckers.phone;

import java.util.Optional;

import ae.phonecheckers.phone.api.model.InventoryVo;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.LockModeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import lombok.Setter;

@Entity
@Setter
@NamedQueries({
        @NamedQuery(name = "Inventory.findAllPhones", query = """
                SELECT
                            inventory.id as id,
                            phone.model as modelName,
                            phone.extRef as extRef,
                            booking.bookedBy as bookedBy,
                            booking.bookedAt as bookingDate
                        FROM
                            Inventory inventory
                        LEFT JOIN
                            inventory.phone phone
                        LEFT JOIN
                            inventory.booking booking
                """),
        @NamedQuery(name = "Inventory.findPhone", query = """
                SELECT
                            inventory.id as id,
                            phone.model as modelName,
                            phone.extRef as extRef,
                            booking.bookedBy as bookedBy,
                            booking.bookedAt as bookingDate
                        FROM
                            Inventory inventory
                        LEFT JOIN
                            inventory.phone phone
                        LEFT JOIN
                            inventory.booking booking
                        WHERE
                            inventory.id = ?1
                """),
		@NamedQuery(name = "Inventory.findAvailablePhoneByModelName", query = """
				SELECT
				            inventory
				        FROM
				            Inventory inventory
				        LEFT JOIN
				            inventory.phone phone
				        LEFT JOIN
				            inventory.booking booking
				        WHERE
				            inventory.phone.model = ?1 AND
							inventory.booking is null
				""")
})
public class Inventory extends PanacheEntity {

    @ManyToOne
    public Phone phone;

    @OneToOne
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

	public static Optional<Inventory> findByModelNameOptional(String modelName, LockModeType lockMode) {
		return find("phone.model = ?1 and booking is null", modelName)
				.withLock(lockMode)
				.firstResultOptional();
	}

	public static Optional<Inventory> findByModel(String modelName, LockModeType lockMode) {
		return Inventory.find("#Inventory.findAvailablePhoneByModelName", modelName)
				.withLock(lockMode)
				.firstResultOptional();
	}
}