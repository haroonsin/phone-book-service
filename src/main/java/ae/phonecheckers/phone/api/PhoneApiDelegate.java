package ae.phonecheckers.phone.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import ae.phonecheckers.phone.Booking;
import ae.phonecheckers.phone.BookingRepository;
import ae.phonecheckers.phone.Inventory;
import ae.phonecheckers.phone.InventoryRepository;
import ae.phonecheckers.phone.Phone;
import ae.phonecheckers.phone.api.model.BookingRequest;
import ae.phonecheckers.phone.api.model.BookingResponse;
import ae.phonecheckers.phone.api.model.InventoryVo;
import ae.phonecheckers.phone.api.model.PhoneVo;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
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

        PanacheQuery<InventoryVo> phones = Inventory.find("""
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
                """).project(InventoryVo.class);
        List<InventoryVo> phoneVos = phones.stream().collect(Collectors.toList());
        if (phoneVos.size() == 0) {
            return Response.noContent().build();
        } else {
            return Response.ok(phoneVos).build();
        }
    }

    @Override
    public Response getPhone(String phoneIdentifier) {
        if (!StringUtils.isNumeric(phoneIdentifier)) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        return inventoryRepository.findByIdOptional(Long.valueOf(phoneIdentifier))
                .map(this::mapToPhoneVo)
                .orElseGet(() -> Response.status(Status.NOT_FOUND).build());
    }

    @Transactional
    @Override
    public Response bookPhone(BookingRequest request) {

        if (!StringUtils.isNumeric(request.getPhoneId())) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        return inventoryRepository.findByIdOptional(Long.valueOf(request.getPhoneId()), LockModeType.PESSIMISTIC_READ)
                .filter(inventory -> inventory.isAvailable())
                .map(inventory -> this.registerBooking(inventory, request))
                .orElseGet(() -> Response.status(Status.NOT_ACCEPTABLE).build());
    }

    @Transactional
    @Override
    public Response returnPhone(String phoneIdentifier) {

        if (!StringUtils.isNumeric(phoneIdentifier)) {
            return Response.status(Status.BAD_REQUEST).build();
        }
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
        bookingRepository.persistAndFlush(newBooking);
        inventory.setBooking(newBooking);
        inventoryRepository.persistAndFlush(inventory);
        return Response.accepted(
                new BookingResponse(bookingRequest.getPhoneId(), String.valueOf(newBooking.id))).build();
    }

    private Response registerReturn(Inventory inventory) {

        Booking currentBooking = inventory.booking;
        bookingRepository.delete(currentBooking);
        inventory.booking = null;
        inventoryRepository.persistAndFlush(inventory);
        return Response.accepted().build();
    }

    private Response mapToPhoneVo(Inventory inventory) {

        Phone phone = inventory.phone;
        String bookedBy = null;
        LocalDateTime bookedAt = null;
        if (!inventory.isAvailable()) {
            Booking booking = inventory.booking;
            bookedBy = booking.getBookedBy();
            bookedAt = booking.getBookedAt();
        }
        return Response
                .ok(new PhoneVo(String.valueOf(inventory.id), phone.getModel(), phone.getExtRef(),
                        inventory.isAvailable(),
                        bookedAt,
                        bookedBy))
                .build();
    }

}
