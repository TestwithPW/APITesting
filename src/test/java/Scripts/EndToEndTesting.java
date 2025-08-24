package Scripts;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.*;
import net.bytebuddy.NamingStrategy.Suffixing.BaseNameResolver.ForGivenType;

public class EndToEndTesting {
	
	 private Integer Id;

	    @Test(priority = 1)
	    public void createUser_extractId() {
	        SoftAssert soft = new SoftAssert();

	        String body = "{ \"name\": \"John Tester\", \"email\": \"john.tester@example.com\" }";

	        Response resp = RestAssured.given()
	                .baseUri("https://jsonplaceholder.typicode.com")
	                .contentType(ContentType.JSON)
	                .body(body)
	            .when()
	                .post("/users")
	            .then()
	                .extract().response();

	        System.out.println("POST Status code: " + resp.getStatusCode());
	        System.out.println("POST Response body:\n" + resp.asPrettyString());

	        soft.assertEquals(resp.getStatusCode(), 201, "POST /users should return 201");

	        try {
	            Id = resp.jsonPath().getInt("id");
	        } catch (Exception ignored) { }
	        soft.assertTrue(Id != null && Id > 0, "Created user id must be > 0");
	        System.out.println("Recently created ID is :" +Id);

	        soft.assertAll();
	    }

	    
	    // Here using id as 1 because 11 it fails and end-points only works for id between 1-10
	    @Test(priority = 2, dependsOnMethods = "createUser_extractId")
	    public void updateUser_put() {
	        SoftAssert soft = new SoftAssert();

	        // valid JSON body
	        System.out.println("Here using id as 1 because 11 it fails and end-points only works for id between 1-10");
	        String body = """
	            {
	              "id": 1,
	              "name": "Updated User",
	              "email": "john.tester@example.com"
	            }
	            """;

	        Response resp = RestAssured.given()
	                .baseUri("https://jsonplaceholder.typicode.com")
	                .contentType(ContentType.JSON)
	                .pathParam("id", 1)   // safe to keep as int
	                .body(body)
	            .when()
	                .put("/users/{id}")
	            .then()
	                .extract().response();

	        System.out.println("PUT Status code: " + resp.getStatusCode());
	        System.out.println("PUT Response body:\n" + resp.asPrettyString());

	        soft.assertEquals(resp.getStatusCode(), 200, "PUT /users/{id} should return 200");

	        try {
	            String name = resp.jsonPath().getString("name");
	            soft.assertEquals(name, "Updated User", "User name should be updated");
	        } catch (Exception ignored) {
	            soft.fail("PUT response missing 'name'");
	        }

	        soft.assertAll();
	    }

	    @Test(priority = 3, dependsOnMethods = "updateUser_put")
	    public void deleteUser() {
	        SoftAssert soft = new SoftAssert();

	        Response resp = RestAssured.given()
	                .baseUri("https://jsonplaceholder.typicode.com")
	                .pathParam("id", 1)
	            .when()
	                .delete("/users/{id}")
	            .then()
	                .extract().response();

	        System.out.println("DELETE Status code: " + resp.getStatusCode());
	        System.out.println("DELETE Response body:\n" + resp.asPrettyString());

	        soft.assertEquals(resp.getStatusCode(), 200, "DELETE /users/{id} should return 200");

	        // jsonplaceholder usually returns {} after delete
	        String body = resp.asString().trim();
	        soft.assertTrue(body.equals("{}") || body.isEmpty(),
	                "DELETE response should be {} or empty, got: " + body);

	        soft.assertAll();
	    }

}
