package qtriptest.APITests;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.RestAssured;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import com.github.fge.jsonschema.messages.JsonSchemaValidationBundle;

public class testCase_API_02 {
    // 1.After successful search, the status code must be 200

    // 2. The Result should be an array of length 1

    // 3. The Description should contain "100+ Places"

    // Validate Schema
    @Test
    public void VerifySearchCity() {
        RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net";
        RestAssured.basePath = "/api/v1/cities";
        RequestSpecification http = RestAssured.given().log().all();
        Response res = http.contentType("application/json").queryParam("q", "beng").when().get();
        List<HashMap<String, String>> list = res.body().jsonPath().getList("$");
        System.out.println(list.get(list.size() - 1));
        System.out.println(list.size());
        Assert.assertEquals(res.getStatusCode(), 200);
        Assert.assertEquals(res.body().jsonPath().getString("description"), "[100+ Places]");
        Assert.assertEquals(list.size(), 1);
        File fileobj = new File("src/test/resources/schema.json");
        JsonSchemaValidator validator = JsonSchemaValidator.matchesJsonSchema(fileobj);

        res.then().assertThat().body(validator);
    }



}
