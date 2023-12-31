package ae.phonecheckers.phone;

import java.time.LocalDateTime;

import ae.phonecheckers.phone.api.model.BookingRequest;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "booking")
public class Booking extends PanacheEntity {

	@OneToOne(mappedBy = "booking", cascade = CascadeType.PERSIST)
	public Inventory inventory;

	@Column(nullable = false)
	private String bookedBy;

	@Column(nullable = false)
	private LocalDateTime bookedAt;

	public static Booking init(String requestor, Inventory inventory) {

		Booking newBooking = new Booking();
		newBooking.setInventory(inventory);
		newBooking.setBookedBy(requestor);
		newBooking.setBookedAt(LocalDateTime.now());
		return newBooking;
	}
}