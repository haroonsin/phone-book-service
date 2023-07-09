package ae.phonecheckers.phone.api;

import java.util.List;

import ae.phonecheckers.phone.Booking;
import ae.phonecheckers.phone.BookingRepository;
import ae.phonecheckers.phone.Inventory;
import ae.phonecheckers.phone.InventoryRepository;
import ae.phonecheckers.phone.api.model.BookingRequest;
import ae.phonecheckers.phone.api.model.BookingResponse;
import ae.phonecheckers.phone.api.model.InventoryVo;
import io.quarkus.logging.Log;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class PhoneApiDelegate implements PhoneApi {

	private InventoryRepository inventoryRepository;
	private BookingRepository bookingRepository;

	public PhoneApiDelegate(InventoryRepository inventoryRepository, BookingRepository bookingRepository) {
		this.inventoryRepository = inventoryRepository;
		this.bookingRepository = bookingRepository;
	}

	@Override
	public Response getAllPhones() {
		List<InventoryVo> phoneVos = InventoryVo.findAll();
		if (phoneVos.size() == 0) {
			return Response.noContent().build();
		} else {
			return Response.ok(phoneVos).build();
		}
	}

	@Override
	public Response getPhone(String phoneIdentifier) {
		return InventoryVo.find(Long.valueOf(phoneIdentifier))
				.map(phone -> Response.ok(phone).build())
				.orElseGet(() -> Response.status(Status.NOT_FOUND).build());
	}

	@Transactional
	@Override
	public Response bookPhone(BookingRequest request) {
		return inventoryRepository.findByIdOptional(Long.valueOf(request.getPhoneId()), LockModeType.PESSIMISTIC_READ)
				.filter(inventory -> inventory.isAvailable())
				.map(inventory -> registerBooking(inventory, request))
				.orElseGet(() -> Response.status(Status.NOT_ACCEPTABLE).build());
	}

	@Transactional
	@Override
	public Response returnPhone(String phoneIdentifier) {
		try {
			return inventoryRepository
					.findByIdOptional(Long.valueOf(phoneIdentifier), LockModeType.PESSIMISTIC_READ)
					.filter(inventory -> inventory.isBooked())
					.map(inventory -> this.registerReturn(inventory))
					.orElseGet(() -> Response.status(Status.NOT_ACCEPTABLE).build());
		} catch (OptimisticLockException exception) {
			Log.errorf(exception, "Unable to process return. Phone id: %s. Reason: %s ", phoneIdentifier,
					exception.getMessage());
			return Response.status(Status.CONFLICT).build();
		}
	}

	private Response registerBooking(Inventory inventory, BookingRequest bookingRequest) {

		Booking newBooking = Booking.init(bookingRequest, inventory);
		inventory.setBooking(newBooking);
		newBooking.persistAndFlush();
		return Response.accepted(new BookingResponse(bookingRequest.getPhoneId(), String.valueOf(newBooking.id)))
				.build();
	}

	private Response registerReturn(Inventory inventory) {

		Booking currentBooking = inventory.booking;
		inventory.booking = null;
		currentBooking.delete();
		return Response.accepted().build();
	}
}
