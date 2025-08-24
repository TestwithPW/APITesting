package Scripts;

import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.response.Response;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class NegativeTesting {
	
	 @Test
	    public void createUser_emptyBody_shouldBeClientError() {
	        SoftAssert soft = new SoftAssert();

	        // send POST with empty JSON body
	        Response resp = RestAssured.given()
	                .baseUri("https://jsonplaceholder.typicode.com")
	                .contentType(ContentType.JSON)    // <- keep this
	                .body("{}")                       // <- NOTE: quotes! a String
	            .when()
	                .post("/users")
	            .then()
	                .extract().response();

	        // print for debug
	        System.out.println("Status code: " + resp.getStatusCode());
	        System.out.println("Response body:");
	        System.out.println(resp.asPrettyString());

	        // assertion: status code should be in 400 range (client error)
	        int code = resp.getStatusCode();
	        soft.assertTrue(code >= 400 && code < 500,
	                "Expected 4xx for empty body, but got " + code);
	        if (code == 201) {
	            System.out.println("JSONPlaceholder mock API accepts empty body, so 201 is returned instead of 400.");
	        }

	        // assertion: response body should contain some error keyword
	        String lower = resp.asString().toLowerCase();
	        soft.assertEquals(lower.contains("error") , true , "Error message should be there");

	        soft.assertAll();
	    }

}
