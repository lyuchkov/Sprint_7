import client.CourierClient;
import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

public class CourierLoginTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }


    @Test
    @DisplayName("Check successful sign in")
    public void successSignInTest() {
        CourierClient courierClient = new CourierClient();
        Courier courier = getSimpleCourierObject();
        courierClient.getCorrectCourierCreationResponse(courier).then().assertThat().statusCode(201);
        Response response =
                courierClient.getSuccessfulSignInResponse(courier);
        assertEquals("Тело ответа: " + response.asString(), 200, response.statusCode());
        assertNotNull(response.path("id"));
        assertThat(response.path("id"), instanceOf(Integer.class));
    }

    @Test
    @DisplayName("Check sign in with wrong login")
    public void singInWithWrongLoginTest() {
        CourierClient courierClient = new CourierClient();
        Courier courier = getSimpleCourierObject();

        courierClient.getCorrectCourierCreationResponse(courier).then().assertThat().statusCode(201);

        courier.setLogin(courier.getLogin() + "wrong");

        Response response = courierClient.getSignInWithWrongLoginResponse(courier);

        assertEquals("Тело ответа: " + response.asString(), 404, response.statusCode());
        assertEquals("Учетная запись не найдена", response.path("message"));
    }

    @Test
    @DisplayName("Check sign in with wrong password")
    public void singInWithWrongPasswordTest() {
        CourierClient courierClient = new CourierClient();
        Courier courier = getSimpleCourierObject();

        courierClient.getCorrectCourierCreationResponse(courier).then().assertThat().statusCode(201);

        courier.setPassword(courier.getPassword() + "wrong");

        Response response = courierClient.getSignInWithWrongPasswordResponse(courier);

        assertEquals("Тело ответа: " + response.asString(), 404, response.statusCode());
        assertEquals("Учетная запись не найдена", response.path("message"));
    }

    @Test
    @DisplayName("Check sign in without login")
    public void singInWithoutLoginTest() {
        CourierClient courierClient = new CourierClient();
        Courier courier = getSimpleCourierObject();

        courierClient.getCorrectCourierCreationResponse(courier).then().assertThat().statusCode(201);

        courier.setLogin(null);

        Response response = courierClient.getSignInWithoutLoginResponse(courier);

        assertEquals("Тело ответа: " + response.asString(), 400, response.statusCode());
        assertEquals("Недостаточно данных для входа", response.path("message"));
    }

    @Test
    @DisplayName("Check sign in without password")
    public void singInWithoutPasswordTest() {
        CourierClient courierClient = new CourierClient();
        Courier courier = getSimpleCourierObject();

        courierClient.getCorrectCourierCreationResponse(courier).then().assertThat().statusCode(201);

        courier.setPassword(null);

        Response response = courierClient.getSignInWithoutPasswordResponse(courier);

        assertEquals("Тело ответа: " + response.asString(), 400, response.statusCode());
        assertEquals("Недостаточно данных для входа", response.path("message"));
    }

    @Step("Get simple Courier object")
    private Courier getSimpleCourierObject() {
        return new Courier("testCourier" + ThreadLocalRandom.current().nextInt(0, 9999999), "1234", "testFirstName");
    }

}
