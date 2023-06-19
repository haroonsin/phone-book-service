package ae.phonecheckers.phone.api.model;

import java.time.LocalDateTime;

public record PhoneVo(
                String id,
                String modelName,
                String extRef,
                boolean isAvailable,
                LocalDateTime bookingDate,
                String bookedBy) {
}
