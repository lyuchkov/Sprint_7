import client.OrderClient;
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
    @DisplayName("Check response for orders list with courier id=1")
    public void getOrdersListWithCourierIdOneTest(){
        OrderClient orderClient = new OrderClient();
        Response response =
               orderClient.getOrdersForCourierWithId("1");

        assertEquals("Тело ответа: " + response.asString(), 200, response.statusCode());
        assertNotNull(response.jsonPath().get("orders"));
        assertNotNull(response.jsonPath().get("pageInfo"));
        assertNotNull(response.jsonPath().get("availableStations"));

    }
    @Test
    @DisplayName("Check response for orders list")
    public void getOrdersListTest(){
        OrderClient orderClient = new OrderClient();
        Response response =
                orderClient.getOrders();

        assertEquals("Тело ответа: " + response.asString(), 200, response.statusCode());
        assertNotNull(response.jsonPath().get("orders"));
        assertNotNull(response.jsonPath().get("pageInfo"));
        assertNotNull(response.jsonPath().get("availableStations"));

    }
}
