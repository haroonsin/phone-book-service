package ae.phonecheckers.phone.api;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import ae.phonecheckers.phone.api.model.BookingRequest;
import ae.phonecheckers.phone.api.model.BookingWithModelRequest;
import ae.phonecheckers.phone.api.model.InventoryVo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/v1")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public interface PhoneApi {

	@GET
	@Path("/phones")
	@Operation(operationId = "GetAllPhones", summary = "Retrieves all phones registered in the app.", description = "Get all phones")
	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "Succesful response", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = InventoryVo.class))),
			@APIResponse(responseCode = "204", description = "No Phones registered in the app", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = InventoryVo.class))) })
	Response getAllPhones();

	@GET
	@Path("/phone/{phoneIdentifier}")
	@Operation(operationId = "GetPhone", summary = "Retrieves phone info for the identifier.", description = "Get the phone info")

	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "Succesful response", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = InventoryVo.class))),
			@APIResponse(responseCode = "400", description = "Invalid phone idenifier. Try numbers from 1 to 10."),
			@APIResponse(responseCode = "404", description = "No Phone found for the identifier") })
	Response getPhone(
			@PathParam("phoneIdentifier") @Parameter(description = "The identifier for the phone", required = true, example = "5", schema = @Schema(implementation = String.class)) @NotBlank @Pattern(regexp = "^[1-9][0-9]*$", message = "Invalid phone identifier") String phoneIdentifier);

	@POST
	@Path("/phone/book")
	@Operation(operationId = "Request phone booking", summary = "Attempts to book a phone if available.", description = "Books the phone if available")
	@APIResponses(value = {
			@APIResponse(responseCode = "202", description = "Succesful response"),
			@APIResponse(responseCode = "400", description = "Invalid input. Try giving number under 10 for phoneId"),
			@APIResponse(responseCode = "406", description = "Invalid phone id. Try giving number under 10"),
			@APIResponse(responseCode = "409", description = "Unable to process request. Please check the status of phone and try again if required")
	})
	Response bookPhone(
			@RequestBody(description = "The reference identifier for the phone", required = true, content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = BookingRequest.class, example = "{\n"
					+ //
					"  \"phoneId\": \"2\",\n" + //
					"  \"requestor\": \"Ab\"\n" + //
					"}"))) @Valid BookingRequest request);

	@POST
	@Path("/phone/book/model")
	@Operation(operationId = "Request phone booking using model name", summary = "Attempts to book a phone using modelname if available.", description = "Books the phone if available")
	@APIResponses(value = {
			@APIResponse(responseCode = "202", description = "Succesful response"),
			@APIResponse(responseCode = "400", description = "Invalid input. Try giving number under 10 for phoneId"),
			@APIResponse(responseCode = "406", description = "Invalid phone id. Try giving number under 10"),
			@APIResponse(responseCode = "409", description = "Unable to process request. Please check the status of phone and try again if required")
	})
	Response bookPhoneByModelName(
			@RequestBody(description = "The model name for the phone", required = true, content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = BookingWithModelRequest.class, example = "{\n"
					+ //
					"  \"modelName\": \"Samsung Galaxy S8\",\n" + //
					"  \"requestor\": \"Ab\"\n" + //
					"}"))) @Valid BookingWithModelRequest request);

	@DELETE
	@Path("/phone/book/{phoneIdentifier}")
	@Operation(operationId = "Request phone return", summary = "Returns the phone and makes it available.", description = "Returns the phone")
	@APIResponses(value = {
			@APIResponse(responseCode = "202", description = "Succesful response"),
			@APIResponse(responseCode = "400", description = "Invalid input. Try giving number under 10 for phoneId"),
			@APIResponse(responseCode = "406", description = "Invalid phone id. Try giving number under 10. Else attempting to return phone which is not booked"),
			@APIResponse(responseCode = "409", description = "Unable to process request. Please check the status of phone and try again if required")
	})
	Response returnPhone(
			@PathParam("phoneIdentifier") @Parameter(description = "The identifier for the phone", required = true, example = "5", schema = @Schema(implementation = String.class)) @NotBlank @Pattern(regexp = "^[1-9][0-9]*$", message = "Invalid phone identifier") String phoneIdentifier);

}
