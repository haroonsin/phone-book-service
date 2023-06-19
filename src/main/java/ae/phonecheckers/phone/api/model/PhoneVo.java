package ae.phonecheckers.phone.api.model;

import java.time.LocalDateTime;

public record PhoneVo(
                String id,
                String modelName,
                boolean isAvailable,
                LocalDateTime bookingDate,
                String bookedBy) {
}
