package qtriptest.APITests;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;
import org.json.JSONObject;
import org.openqa.selenium.devtools.v85.page.Page.SetTouchEmulationEnabledConfiguration;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.RestAssured;
import java.util.UUID;



public class testCase_API_01 {
    RequestSpecification http;
    public String email;
    public  String password;
    public String token;
    public String userId;

    @Test
    public void Register() {
        RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net";
        RestAssured.basePath = "/api/v1/register";
        email = "Soumya" + UUID.randomUUID().toString() + "@email.com";
        password = UUID.randomUUID().toString();

        JSONObject obj = new JSONObject();
        obj.put("email", email);
        obj.put("password", password);
        obj.put("confirmpassword", password);
        http = RestAssured.given().log().all();
        Response res = http.contentType("application/json").body(obj.toString()).when().post();
        System.out.println(res.getStatusCode());
        System.out.println(res.getStatusLine());
        Assert.assertEquals(res.getStatusCode(), 201);

    }
@Test
    public void login() {
        RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net";
        RestAssured.basePath = "/api/v1/login";
        JSONObject obj = new JSONObject();
        obj.put("email", email);
        obj.put("password", password);
        http = RestAssured.given().log().all();
        Response res = http.contentType("application/json").body(obj.toString()).when().post();
        System.out.println(res.getStatusCode());
        System.out.println(res.getStatusLine());
        token = res.body().jsonPath().getString("data.token");
        System.out.println(token);
         userId=  res.body().jsonPath().getString("data.id");
        System.out.println(userId);
        Assert.assertEquals(res.getStatusCode(), 201, "Status code does not match expected value.");
        Assert.assertTrue(res.body().jsonPath().getBoolean("success"));
    }
}
