package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import Payloads.*;
import EndPoints.HttpMethods;;

public class UserTest {
	static HttpMethods httpMethods;
	static Faker faker;
	static PojoClass pojoClass; 
	static String bearerToken;
	static int account_id;
	@BeforeClass
	public void generateDataAndGetToken() {
		String description =" Test descriptino test";
		faker = new Faker();
		pojoClass = new PojoClass();
		pojoClass.setAccount_name(faker.name().fullName());
		pojoClass.setDescription(faker.country().capital());
		pojoClass.setBalance((faker.number().randomDouble(2, 100, 1000)));
		pojoClass.setAccount_number(faker.number().numberBetween(1, 80));
		pojoClass.setContact_person(faker.name().fullName());
		httpMethods = new HttpMethods();
		bearerToken = httpMethods.generateToken();
	}
	@Test (priority = 1)
	public void createUser() {

		System.out.println("*************Beaer TOken: "+ bearerToken);
		Response response = httpMethods.createUser(bearerToken, pojoClass);
		
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 201);
		Assert.assertEquals(HttpMethods.message, "Account created successfully.");
	}
	
	
	@Test (priority = 2, dependsOnMethods = "createUser")
	public  void getAllUsersAndGenerateId() {
		Response response = httpMethods.getAllUsers(bearerToken);
		JsonPath jp = new JsonPath(response.getBody().asString());
		this.account_id = jp.getInt("records[0].account_id");

		Assert.assertEquals(response.getStatusCode(), 200);
		
	}
	
	@Test (priority = 3)
	public void getUserById() {
		
		Response response = httpMethods.getSingleUser(bearerToken, account_id);
		System.out.println(" Bearer Token is "+ httpMethods.bearerToken);
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Test(priority =4)
	public void updateUser(){
		pojoClass = new PojoClass();
		pojoClass.setAccount_name(faker.name().fullName());
		pojoClass.setDescription(faker.country().capital());
		pojoClass.setBalance((faker.number().randomDouble(2, 100, 1000)));
		pojoClass.setAccount_number(faker.number().numberBetween(1, 80));
		pojoClass.setContact_person(faker.name().fullName());
		Response response = httpMethods.updateUser(bearerToken, account_id, pojoClass);
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(HttpMethods.message, "Account updated successfully");
		System.out.println("Account id ************"+account_id);
		System.out.println("************************ "+ "After Update" + "************************");
//		httpMethods.getSingleUser(bearerToken, account_id);
		
	}
	@Test(priority = 5, dependsOnMethods = "updateUser")
	public void printUpdatedUser() {
	    System.out.println("************************ " + "After Update" + "************************");
	    httpMethods.getSingleUser(bearerToken, account_id);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
