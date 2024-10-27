package qtriptest.APITests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import java.util.UUID;
import org.json.JSONObject;
import org.testng.annotations.Test;

public class testCase_API_03 {
    @Test(
        description = "Verify that a reservation can be made using the QTrip API",
        enabled = true,
        priority = 3,
        groups = {"API Tests"}
    )
    public void TestCase03() {
        // Register
        RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net";
        RestAssured.basePath = "/api/v1";

        String email = String.format("user%s@crio.com", UUID.randomUUID());
        String password = UUID.randomUUID().toString();

        RequestSpecification req = RestAssured.given();

        JSONObject obj = new JSONObject();
        obj.put("email", email);
        obj.put("password", password);
        obj.put("confirmpassword", password);

        req.header("Content-Type","application/json");
        req.body(obj.toString());

        Response resp = req.when().post("/register");

        // Verify status code to be 201
        resp.then().statusCode(201);

        // Login
        obj.remove("confirmpassword");
        req.body(obj.toString());

        resp = req.when().post("/login");

        // Verify success is true
        resp.then().body("success", equalTo(true));

        // Verify token is generated
        resp.then().body("data.token", notNullValue());

        // Verify id is generated
        resp.then().body("data.id", notNullValue());

        // Verify status code to be 201
        resp.then().statusCode(201);

        // Reservations
        JsonPath jp = new JsonPath(resp.getBody().asString());

        String userId = jp.getString("data.id");
        String token = jp.getString("data.token");

        req.header("Content-Type","application/json");
        req.header("Authorization", "Bearer "+ token);

        JSONObject reservation_details = new JSONObject();
        reservation_details.put("userId", userId);
        reservation_details.put("name", "TestUser");
        reservation_details.put("date", "2025-10-12");
        reservation_details.put("person", "2");
        reservation_details.put("adventure","2447910730");

        req.body(reservation_details.toString());

        Response reservation_details_response = req.when().post("/reservations/new");
        reservation_details_response.then().statusCode(200);

        // Verifying reservation
        req.queryParam("id", userId);

        reservation_details_response = req.when().get("/reservations");
        reservation_details_response.then().body("isCancelled[0]", equalTo(false));
        reservation_details_response.then().body("userId[0]", equalTo(userId));
        reservation_details_response.then().body("name[0]", equalTo("Testuser"));
    }
}
