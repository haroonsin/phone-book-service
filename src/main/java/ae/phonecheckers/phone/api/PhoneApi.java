package ae.phonecheckers.phone.api;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import ae.phonecheckers.phone.api.model.BookingRequest;
import ae.phonecheckers.phone.api.model.PhoneVo;
import jakarta.ws.rs.Consumes;
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
                        @APIResponse(responseCode = "200", description = "Succesful response", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = PhoneVo.class))),
                        @APIResponse(responseCode = "204", description = "No Phones registered in the app", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = PhoneVo.class))) })
        Response getAllPhones();

        @GET
        @Path("/phone/{phoneIdentifier}")
        @Operation(operationId = "GetPhone", summary = "Retrieves phone info for the identifier.", description = "Get the phone info")

        @APIResponses(value = {
                        @APIResponse(responseCode = "200", description = "Succesful response", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = PhoneVo.class))),
                        @APIResponse(responseCode = "404", description = "No Phone found for the identifier") })
        Response getPhone(
                        @PathParam("phoneIdentifier") @Parameter(description = "The identifier for the phone", required = true, example = "Eg: 1 .. 10 ", schema = @Schema(implementation = String.class)) String phoneIdentifier);

        @POST
        @Path("/phone/book")
        @Operation(operationId = "Request phone booking", summary = "Attempts to book a phone if available. Uses human friendly model ref to make the booking.", description = "Books the phone if available")

        @APIResponses(value = {
                        @APIResponse(responseCode = "201", description = "Succesful response"),
                        @APIResponse(responseCode = "400", description = "Couldn't book phone") })
        Response bookPhone(
                        @Parameter(description = "The reference identifier for the phone", required = true, example = "Eg: samsunggalaxys9 / appleiphone13 etc", schema = @Schema(implementation = BookingRequest.class)) BookingRequest request);

}
