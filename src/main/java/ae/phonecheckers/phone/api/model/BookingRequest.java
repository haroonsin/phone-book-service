package ae.phonecheckers.phone.api.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    @NotBlank
    @Pattern(regexp = "^[1-9][0-9]*$", message = "Invalid phone identifier")
    private String phoneId;
    private String requestor = "Default";
}
