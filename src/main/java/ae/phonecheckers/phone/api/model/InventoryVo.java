package ae.phonecheckers.phone.api.model;

import java.time.LocalDateTime;

import io.quarkus.runtime.annotations.RegisterForReflection;

// @JsonPropertyOrder({ "id", "extRef", "modelName", "isAvailable", "bookedBy", "bookingDate" })
@RegisterForReflection
public record InventoryVo(
        Long id,
        String modelName,
        String extRef,
        boolean isAvailable,
        String bookedBy,
        LocalDateTime bookingDate) {

    public InventoryVo(Long id,
            String modelName,
            String extRef,
            String bookedBy,
            LocalDateTime bookingDate) {
        this(id, modelName, extRef, bookedBy == null, bookedBy, bookingDate);
    }
}
