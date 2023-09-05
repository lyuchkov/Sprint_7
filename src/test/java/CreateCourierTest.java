import client.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check response message for correct courier creation")
    public void createNewCourierTest() {
        CourierClient courierClient = new CourierClient();
        Response response = courierClient.getCorrectCourierCreationResponse(new Courier("naruto" + ThreadLocalRandom.current().nextInt(0, 9999999), "1234", "saske"));
        assertEquals("Тело ответа: " + response.asString(), 201, response.statusCode());
        assertTrue(response.path("ok"));
    }

    @Test
    @DisplayName("Check response message for two equal courier creation")
    public void createTwoEqualCouriers() {
        CourierClient courierClient = new CourierClient();
        Courier courier = new Courier("naruto" + ThreadLocalRandom.current().nextInt(0, 9999999), "1234", "saske");
        Response response = courierClient.getTwoEqualCouriersCreationResponse(courier);
        assertEquals("Тело ответа: " + response.asString(), 409, response.statusCode());
        assertEquals("Этот логин уже используется. Попробуйте другой.", response.path("message"));
    }

    @Test
    @DisplayName("Проверка создания курьера без логина")
    public void createCourierWithoutLogin() {
        CourierClient courierClient = new CourierClient();
        Response response = courierClient.getWitoutLoginCourierCreationResponse(new Courier(null, "234", "saske"));
        assertEquals("Тело ответа: " + response.asString(), 400, response.statusCode());
        assertEquals("Недостаточно данных для создания учетной записи", response.path("message"));
    }

    @Test
    @DisplayName("Проверка создания курьера без пароля")
    public void createCourierWithoutPassword() {
        CourierClient courierClient = new CourierClient();
        Response response = courierClient.getWithoutPasswordCourierCreationResponse(new Courier("naruto" + ThreadLocalRandom.current().nextInt(0, 9999999), null, "saske"));
        assertEquals("Тело ответа: " + response.asString(), 400, response.statusCode());
        assertEquals("Недостаточно данных для создания учетной записи", response.path("message"));
    }

    @Test
    @DisplayName("Проверка создания курьера без имени")
    public void createCourierWithoutFirstName() {
        CourierClient courierClient = new CourierClient();
        Response response = courierClient.getWithoutFirstNameCourierCreationResponse(new Courier("naruto" + ThreadLocalRandom.current().nextInt(0, 9999999), "1234", null));
        assertEquals("Тело ответа: " + response.asString(), 400, response.statusCode());
        assertEquals("Недостаточно данных для создания учетной записи", response.path("message"));
    }

    @Test
    @DisplayName("Проверка создания уже созданного курьера")
    public void createAlreadyCreatedCourier() {
        CourierClient courierClient = new CourierClient();
        Response response = courierClient.getAlreadyCreatedCourierResponse(new Courier("ninja", "1234", "saske"));
        assertEquals("Тело ответа: " + response.asString(), 409, response.statusCode());
        assertEquals("Этот логин уже используется. Попробуйте другой.", response.path("message"));

    }

    @Test
    @DisplayName("Check server connection")
    public void connectionTest() {
        Response response =
                given()
                        .get("/api/v1/ping");
        response.then().assertThat().statusCode(200);
    }
}
