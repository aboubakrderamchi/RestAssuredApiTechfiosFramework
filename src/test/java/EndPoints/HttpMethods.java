package EndPoints;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.ResourceBundle;

import Payloads.PojoClass;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class HttpMethods {
	public static String bearerToken;
	public static String authBody;
	public static String createAccountBodyFilePath;
	public static String message;
	
	
	
	public static ResourceBundle getUrlFromPropertiesFile() {
		ResourceBundle file = ResourceBundle.getBundle("file");
		return file;
	}

	public  String generateToken() {
		authBody = "src/test/resources/auth.json";
		String postUrl = getUrlFromPropertiesFile().getString("post_endpoint");
		Response response = given().header("Content-Type", "application/json").body(new File(authBody))
				.when()
				.post(postUrl)
				.then().extract().response();
		JsonPath jp = new JsonPath(response.getBody().asString());
		this.bearerToken = jp.getString(("access_token"));
		return bearerToken;
	}
	
	//Create Account first then go to get
	
	public Response createUser(String bearerToken, PojoClass pojoClass) {
		createAccountBodyFilePath = "src/test/resources/createAccountBodyFilePath.json";
		String createUrl = getUrlFromPropertiesFile().getString("create_endpoint");
		Response response = given().header("Content-Type", "application/json").header("Authorization", ("Bearer "+ bearerToken)).body(pojoClass).log().all()
			
				.when().post(createUrl)
		.then().extract().response();
		JsonPath jp = new JsonPath(response.getBody().asString());

		this.message = jp.getString("message");
		return response;
	}

	public Response getAllUsers(String bearerToken) {
		String getAllUrl= getUrlFromPropertiesFile().getString("get_All_endpoint");
		Response response = given().header("Content-Type", "application/json").header("Authorization", "Bearer "+ bearerToken)
				.when().get(getAllUrl)
				.then().extract().response();

		return response;
	}
	
	
	
	public Response getSingleUser(String bearerToken, int account_id) {
		String getUrl = getUrlFromPropertiesFile().getString("get_One_endpoint");
		Response response = given().header("Content-Type", "application/json")
				.header("Authorization", "Bearer " + bearerToken).queryParam("account_id", account_id)
				.when()
				.get(getUrl)
				.then().log().all().extract().response();
		return response;
	}
	
	public Response updateUser(String bearerToken, int account_id, PojoClass pojoClass) {
		
		String updateUrl =getUrlFromPropertiesFile().getString("update_endpoint");
		Response response = given().header("Content-Type", "application/json").header("Authorization", bearerToken).queryParam("account_id", account_id).body(pojoClass)
				.when().put(updateUrl)
				.then().log().all().extract().response();
		JsonPath jp = new JsonPath(response.getBody().asString());
		this.message = jp.getString("message");
		return response;
	}
	


}
