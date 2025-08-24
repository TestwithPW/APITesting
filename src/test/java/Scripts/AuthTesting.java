package Scripts;


import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*; // all RestAssured statics

public class AuthTesting {

	private String token;
	private int userId;
	

	@Test 
	public void authtest() {
		SoftAssert sf= new SoftAssert();
		String jbody = """
				
				{ "username: admin",
				 "password: password" 
				 }
				 """;

		Response rs = RestAssured.given()
						.baseUri("https://api.restful-api.dev")
						.contentType("ContentType")
						.body(jbody)
					.when()
						.post("/auth")
					.then()
						.statusCode(200)
						.extract()
						.response();

		// for status code
		sf.assertEquals(rs.statusCode(), 200, "Expected Status code 200 from /auth");

		// for token
		String tok = null;
		try {
			rs.jsonPath().get("token");
		} catch (Exception ignored) {

		}

		// Checking if token is present
		sf.assertNotNull(tok, "token must be present");

		// Checking if token is not empty
		if (tok != null) {
			sf.assertFalse(tok.isEmpty(), "Token must not be empty");
		}

		token = tok;

		sf.assertAll();

	}
	
	
	@Test(priority = 2)
	public void getUsers_headerAndBodyChecks() {
	    SoftAssert soft = new SoftAssert();

	    Response resp = given()
	            .baseUri("https://jsonplaceholder.typicode.com")
	            .header("X-API-KEY", token == null ? "" : token) // harmless for this API
	        .when().get("/users")
	        .then().extract().response();

	    // print status code
	    System.out.println("Status code: " + resp.getStatusCode());

	    // print JSON body
	    System.out.println("GET /users response body:");
	    System.out.println(resp.asPrettyString());

	    // assertions
	    soft.assertEquals(resp.getStatusCode(), 200, "GET /users should be 200");

	    int size = 0;
	    try { size = resp.jsonPath().getList("$").size(); } catch (Exception ignored) {}
	    soft.assertTrue(size > 0, "Users response should not be empty");

	    soft.assertAll();
	}
	
	
	
	

}
