package ae.phonecheckers.phone.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ae.phonecheckers.phone.Booking;
import ae.phonecheckers.phone.BookingRepository;
import ae.phonecheckers.phone.Inventory;
import ae.phonecheckers.phone.InventoryRepository;
import ae.phonecheckers.phone.Phone;
import ae.phonecheckers.phone.PhoneRepository;
import ae.phonecheckers.phone.api.model.PhoneVo;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class PhoneApiDelegate implements PhoneApi {

    @Inject
    PhoneRepository phoneRepository;

    @Inject
    InventoryRepository inventoryRepository;

    @Inject
    BookingRepository bookingRepository;

    @Override
    public Response getAllPhones() {

        List<PhoneVo> phones = phoneRepository.findAll().stream()
                .flatMap(phone -> this.mapToPhoneVos(phone))
                .collect(Collectors.toList());
        if (phones.size() == 0) {
            return Response.noContent().build();
        } else {
            return Response.ok(phones).build();
        }
    }

    @Override
    public Response getPhone(String phoneIdentifier) {
        return inventoryRepository.findByIdOptional(Long.valueOf(phoneIdentifier))
                .map(this::mapToPhoneVo)
                .orElseGet(() -> Response.status(Status.NOT_FOUND).build());
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
                .ok(new PhoneVo(String.valueOf(phone.id), phone.model, inventory.isAvailable(), bookedAt, bookedBy))
                .build();
    }

    private Stream<PhoneVo> mapToPhoneVos(Phone phone) {
        return phone.inventory.stream().map(inventory -> {
            String bookedBy = null;
            LocalDateTime bookedAt = null;
            if (!inventory.isAvailable()) {
                Booking booking = inventory.booking;
                bookedBy = booking.getBookedBy();
                bookedAt = booking.getBookedAt();
            }
            return new PhoneVo(String.valueOf(phone.id), phone.model, inventory.isAvailable(), bookedAt, bookedBy);
        });
    }
}
