package client;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierClient {
    private final Gson gson = new Gson();

    @Step("Get response for creating new valid courier")
    public Response getCorrectCourierCreationResponse(Object body){
        return getBasicResponseV1(body);
    }
    @Step("Get response for creating two equal couriers")
    public Response getTwoEqualCouriersCreationResponse(Object body){
        given()
                .header("Content-type", "application/json")
                .and()
                .body(gson.toJson(body))
                .when()
                .post("/api/v1/courier")
                .then().assertThat().statusCode(201);
        return getBasicResponseV1(body);
    }
    @Step("Get response for creating courier without login")
    public Response getWitoutLoginCourierCreationResponse(Object body){
        return getBasicResponseV1(body);
    }
    @Step("Get response for creating courier without login")
    public Response getWithoutPasswordCourierCreationResponse(Object body){
        return getBasicResponseV1(body);
    }
    @Step("Get response for creating courier without first name")
    public Response getWithoutFirstNameCourierCreationResponse(Object body){
        return getBasicResponseV1(body);
    }
    @Step("Get response for already created courier")
    public Response getAlreadyCreatedCourierResponse(Object body){
        return getBasicResponseV1(body);
    }
    @Step("Get response for post request /api/v1/courier")
    private Response getBasicResponseV1(Object body){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(gson.toJson(body))
                .when()
                .post("/api/v1/courier");
    }

    @Step("Get response for successful sign in")
    public Response getSuccessfulSignInResponse(Object body){
        return getBasicLoginResponseV1(body);
    }
    @Step("Get response for post request /api/v1/courier/login")
    private Response getBasicLoginResponseV1(Object body){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(gson.toJson(body))
                .when()
                .post("/api/v1/courier/login");
    }
    @Step("Get response for sign in with wrong login")
    public Response getSignInWithWrongLoginResponse(Object body) {
        return getBasicLoginResponseV1(body);
    }
    @Step("Get response for sign in with wrong password")
    public Response getSignInWithWrongPasswordResponse(Object body) {
        return getBasicLoginResponseV1(body);
    }
    @Step("Get response for sign in without password")
    public Response getSignInWithoutPasswordResponse(Object body) {
        return getBasicLoginResponseV1(body);
    }
    @Step("Get response for sign in without login")
    public Response getSignInWithoutLoginResponse(Object body) {
        return getBasicLoginResponseV1(body);
    }
}
