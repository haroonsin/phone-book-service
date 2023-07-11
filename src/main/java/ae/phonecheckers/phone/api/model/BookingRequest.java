package ae.phonecheckers.phone.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record BookingRequest(
		@NotBlank @Pattern(regexp = "^[1-9][0-9]*$", message = "Invalid phone identifier") String phoneId,
		String requestor) {
}
