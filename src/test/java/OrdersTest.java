import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OrdersTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка получения списка всех заказов у курьера с id=1")
    public void getOrdersListTest(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .queryParam("courierId", "1")
                        .when()
                        .post("/api/v1/orders")
                        .then().extract().response();
        assertEquals("Тело ответа: " + response.asString(), 200, response.statusCode());
        response.print();
        assertNotNull(response.jsonPath().get("orders"));
        assertNotNull(response.jsonPath().get("pageInfo"));
        assertNotNull(response.jsonPath().get("availableStations"));

    }
}
