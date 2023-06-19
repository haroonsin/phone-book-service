package ae.phonecheckers;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.json.bind.JsonbBuilder;

import org.junit.jupiter.api.Test;

import ae.phonecheckers.phone.api.model.BookingRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.Matchers.*;

@QuarkusTest
public class PhoneApiDelegateTest {

    @Test
    public void shouldReturn200WithAllPhones() {
        given()
                .when().get("/v1/phones")
                .then()
                .statusCode(200)
                .body("$", hasSize(10));
    }

    @Test
    public void shouldReturnPhoneInfoForPhoneIdentifier() {
        String phoneId = "2";
        given()
                .when().get("/v1/phone/" + phoneId)
                .then()
                .statusCode(200)
                .body("$", hasKey("modelName"))
                .body("isAvailable", is(true))
                .body("id", is(phoneId));
    }

    @Test
    public void shouldReturn400ForInvalidPhoneIdentifier() {
        String phoneId = "INVALID";
        given()
                .when().get("/v1/phone/" + phoneId)
                .then()
                .statusCode(400);
    }

    @Test
    public void shouldReturn404ForIncorrectPhoneIdentifier() {
        String phoneId = "20";
        given()
                .when().get("/v1/phone/" + phoneId)
                .then()
                .statusCode(404);
    }

    @Test
    public void shouldBookSpecifiedPhone() {

        String phoneId = "2";
        String requestor = "A";
        given()
                .contentType(ContentType.JSON)
                .body(new BookingRequest(phoneId, requestor))
                .when().post("/v1/phone/book")
                .then()
                .statusCode(202)
                .body("phoneId", is(phoneId))
                .body("$", hasKey("bookingReference"));
    }

    @Test
    public void shouldFailWhenRebookingAlreadyBookedPhone() {

        String phoneId = "3";
        String requestor = "B";
        var bookingRequest = new BookingRequest(phoneId, requestor);
        given()
                .contentType(ContentType.JSON)
                .body(bookingRequest)
                .when().post("/v1/phone/book")
                .then()
                .statusCode(202)
                .body("phoneId", is(phoneId))
                .body("$", hasKey("bookingReference"));

        given()
                .contentType(ContentType.JSON)
                .body(bookingRequest)
                .when().post("/v1/phone/book")
                .then()
                .statusCode(406);

    }

}