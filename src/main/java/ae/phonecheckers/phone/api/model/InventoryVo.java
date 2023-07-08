package ae.phonecheckers.phone.api.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import ae.phonecheckers.phone.Inventory;
import io.quarkus.runtime.annotations.RegisterForReflection;

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

    public static List<InventoryVo> findAll() {

        return Inventory.find("#Inventory.findAllPhones")
                .project(InventoryVo.class)
                .list();
    }

    public static Optional<InventoryVo> find(Long phoneIdentifier) {

        return Inventory.find("#Inventory.findPhone", phoneIdentifier)
                .project(InventoryVo.class).singleResultOptional();
    }
}
