import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

public class CourierLoginTest {
    private final Gson gson = new Gson();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Step("Создать новый аккаунт курьера со случайным логином")
    public Courier createNewCourier(){
        Courier courier = new Courier("testCourier" + ThreadLocalRandom.current().nextInt(0, 9999999), "1234","testFirstName");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(gson.toJson(courier))
                        .when()
                        .post("/api/v1/courier");
        assertTrue(response.path("ok"));
        assertEquals("Тело ответа: " + response.asString(), 201, response.statusCode());
        return courier;
    }
    @Test
    @DisplayName("Проверка успешного входа")
    public void successSignInTest(){
        Courier courier = createNewCourier();
        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .with()
                        .body(gson.toJson(courier))
                        .post("/api/v1/courier/login");
        assertNotNull(response.path("id"));
        assertThat(response.path("id"), instanceOf(Integer.class));
        assertEquals("Тело ответа: " + response.asString(), 200, response.statusCode());
    }
    @Test
    @DisplayName("Проверка входа с некорректным логином")
    public void singInWithWrongLoginTest(){
        Courier courier = createNewCourier();
        courier.setLogin(courier.getLogin()+"wrong");
        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .with()
                        .body(gson.toJson(courier))
                        .post("/api/v1/courier/login");
        assertEquals("Учетная запись не найдена",response.path("message"));
        assertEquals("Тело ответа: " + response.asString(), 404, response.statusCode());
    }
    @Test
    @DisplayName("Проверка входа с неверным паролем")
    public void singInWithWrongPasswordTest(){
        Courier courier = createNewCourier();
        courier.setPassword(courier.getPassword()+"wrong");
        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .with()
                        .body(gson.toJson(courier))
                        .post("/api/v1/courier/login");
        assertEquals("Учетная запись не найдена",response.path("message"));
        assertEquals("Тело ответа: " + response.asString(), 404, response.statusCode());
    }
    @Test
    @DisplayName("Проверка входа без логина")
    public void singInWithoutLoginTest(){
        Courier courier = createNewCourier();
        courier.setLogin(null);
        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .with()
                        .body(gson.toJson(courier))
                        .post("/api/v1/courier/login");
        assertEquals("Недостаточно данных для входа",response.path("message"));
        assertEquals("Тело ответа: " + response.asString(), 400, response.statusCode());
    }
    @Test
    @DisplayName("Проверка входа без пароля")
    public void singInWithoutPasswordTest(){
        Courier courier = createNewCourier();
        courier.setPassword(null);
        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .with()
                        .body(gson.toJson(courier))
                        .post("/api/v1/courier/login");
        assertEquals("Недостаточно данных для входа",response.path("message"));
        assertEquals("Тело ответа: " + response.asString(), 400, response.statusCode());
    }


}
