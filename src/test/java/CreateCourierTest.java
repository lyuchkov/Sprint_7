import com.google.gson.Gson;
import io.qameta.allure.Step;
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
    private final Gson gson = new Gson();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка создания курьера")
    public void createNewCourierTest() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(gson.toJson(createCourierWithParams("naruto" + ThreadLocalRandom.current().nextInt(0, 9999999), "1234", "saske")))
                        .when()
                        .post("/api/v1/courier");
        assertTrue(response.path("ok"));
        assertEquals("Тело ответа: " + response.asString(), 201, response.statusCode());

    }

    @Test
    @DisplayName("Проверка создания двух одинаковых курьеров")
    public void createTwoEqualCouriers() {
        Courier courier = createCourierWithParams("naruto" + ThreadLocalRandom.current().nextInt(0, 9999999), "1234", "saske");
        given()
                .header("Content-type", "application/json")
                .and()
                .body(gson.toJson(courier))
                .when()
                .post("/api/v1/courier");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(gson.toJson(courier))
                        .when()
                        .post("/api/v1/courier");
        assertEquals("Этот логин уже используется. Попробуйте другой.", response.path("message"));
        assertEquals("Тело ответа: " + response.asString(), 409, response.statusCode());
    }

    @Test
    @DisplayName("Проверка создания курьера без логина")
    public void createCourierWithoutLogin() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(gson.toJson(createCourierWithParams(null, "234", "saske")))
                        .when()
                        .post("/api/v1/courier");
        assertEquals("Недостаточно данных для создания учетной записи", response.path("message"));
        assertEquals("Тело ответа: " + response.asString(), 400, response.statusCode());

    }

    @Test
    @DisplayName("Проверка создания курьера без пароля")
    public void createCourierWithoutPassword() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(gson.toJson(createCourierWithParams("naruto" + ThreadLocalRandom.current().nextInt(0, 9999999), null, "saske")))
                        .when()
                        .post("/api/v1/courier");
        assertEquals("Недостаточно данных для создания учетной записи", response.path("message"));
        assertEquals("Тело ответа: " + response.asString(), 400, response.statusCode());

    }

    @Test
    @DisplayName("Проверка создания курьера без имени")
    public void createCourierWithoutFirstName() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(gson.toJson(createCourierWithParams("naruto" + ThreadLocalRandom.current().nextInt(0, 9999999), "1234", null)))
                        .when()
                        .post("/api/v1/courier");
        assertEquals("Недостаточно данных для создания учетной записи", response.path("message"));
        assertEquals("Тело ответа: " + response.asString(), 400, response.statusCode());
    }

    @Test
    @DisplayName("Проверка создания уже созданного курьера")
    public void createAlreadyCreatedCourier() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(gson.toJson(createCourierWithParams("ninja", "1234", "saske")))
                        .when()
                        .post("/api/v1/courier");
        assertEquals("Этот логин уже используется. Попробуйте другой.", response.path("message"));
        assertEquals("Тело ответа: " + response.asString(), 409, response.statusCode());

    }

    @Test
    @DisplayName("Проверка наличия соединения с сервером")
    public void connectionTest() {
        Response response =
                given()
                        .get("/api/v1/ping");
        response.then().assertThat().statusCode(200);
    }

    @Step("Создание курьера с нужными параметрами")
    private Courier createCourierWithParams(String login, String password, String firstName) {
        return new Courier(login, password, firstName);
    }
}
