package qtriptest.APITests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

public class testCase_API_04 {
  //String baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1/register"; 
   
  RequestSpecification http;
     
    @Test
    public void testUserRegistration() {
        RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net";
        RestAssured.basePath = "/api/v1/register";
        http = RestAssured.given().log().all();
        
        String email = "Soumya" + UUID.randomUUID().toString() + "@email.com"; 
        JSONObject newUser = new JSONObject();
        newUser.put("email", email);
        newUser.put("password", "Password123"); 
        newUser.put("confirmpassword", "Password123");

        Response response = http.contentType("application/json").body(newUser.toString()).when().post();
        System.out.println("First registration status code: " + response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 201, "Expected status code 201 for successful registration");

       
        JSONObject duplicateUser = new JSONObject();
        duplicateUser.put("email", email);
        duplicateUser.put("password", "Password123");
        duplicateUser.put("confirmpassword", "Password123");

        Response duplicateResponse =http.contentType("application/json").body(duplicateUser.toString()).when().post();
        System.out.println("Second registration status code: " + duplicateResponse.getStatusCode());
        Assert.assertEquals(duplicateResponse.getStatusCode(), 400, "Expected status code 400 for duplicate email registration");

        
        String errorMessage = duplicateResponse.jsonPath().getString("message"); 
        Assert.assertEquals(errorMessage, "Email already exists", "Expected error message for duplicate email");
    }

    

    }


  

