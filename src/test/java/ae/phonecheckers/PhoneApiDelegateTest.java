package ae.phonecheckers;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

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
                .body("$", hasSize(11));
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

}