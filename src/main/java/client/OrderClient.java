package client;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private final Gson gson = new Gson();

    @Step("Get response for order creation")
    public Response getOrderCreationResponse(Object body){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(gson.toJson(body))
                .when()
                .post("/api/v1/orders");
    }

    @Step("Get response for orders with courier that have id from parameter")
    public Response getOrdersForCourierWithId(String id){
        return given()
                .header("Content-type", "application/json")
                .queryParam("courierId", id)
                .when()
                .post("/api/v1/orders")
                .then().extract().response();
    }
    @Step("Get response for orders list")
    public Response getOrders(){
        return given()
                .header("Content-type", "application/json")
                .when()
                .post("/api/v1/orders")
                .then().extract().response();
    }
}
