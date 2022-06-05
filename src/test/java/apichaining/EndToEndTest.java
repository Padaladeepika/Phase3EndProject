package apichaining;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EndToEndTest {
	
	Response response;
	String BaseURI = "http://18.215.150.59:8088/employees";

	@Test
	public void test1() {

		response = GetMethodAll();
		AssertJUnit.assertEquals(response.getStatusCode(), 200);
		
		response = PostMethod("Prathap","Reddy","6000","PR@abc.com");
		AssertJUnit.assertEquals(response.getStatusCode(), 201);
		JsonPath Jpath = response.jsonPath();
		int EmpId = Jpath.get("id");
		System.out.println("id :-" + EmpId);
		
		response = PutMethod(EmpId,"Deepika","Padala","6000","DP@abc.com");
		AssertJUnit.assertEquals(response.getStatusCode(), 200);
		Jpath = response.jsonPath();
		AssertJUnit.assertEquals(Jpath.get("firstName"), "Deepika");

		  response = DeleteMethod(EmpId);
	        String ResponseBody = response.getBody().asString();
			Assert.assertEquals(ResponseBody, "");	
			int ResponseCode = response.getStatusCode();
			Assert.assertEquals(ResponseCode, 200);
			
			response = GetMethod(EmpId);
		    Assert.assertEquals(response.getStatusCode(), 400);
			Jpath =response.jsonPath();
			Assert.assertEquals(Jpath.get("message"), "Entity Not Found");
	}
	public Response GetMethodAll() {
		RestAssured.baseURI = BaseURI;

		RequestSpecification request = RestAssured.given();

		Response response = request.get();

		return response;
	}
	public Response PostMethod(String firstName, String lastName, String salary, String email) {

		RestAssured.baseURI = BaseURI;

		JSONObject jobj = new JSONObject();
		jobj.put("firstName", firstName);
		jobj.put("lastName", lastName);
		jobj.put("salary", salary);
        jobj.put("email", email);

		RequestSpecification request = RestAssured.given();

		Response response = request.contentType(ContentType.JSON).accept(ContentType.JSON).body(jobj.toString())
				.post("");

		return response;
				
	}
	
	public Response PutMethod(int EmpId ,String firstName, String lastName, String salary, String email) {

		RestAssured.baseURI = BaseURI;

		JSONObject jobj = new JSONObject();
		jobj.put("firstName", firstName);
		jobj.put("lastName", lastName);
		jobj.put("salary", salary);
        jobj.put("email", email);


		RequestSpecification request = RestAssured.given();

		Response response = request.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(jobj.toString())
				.put("/" + EmpId);
		return response;
	}

	public Response DeleteMethod(int EmpId) {

		RestAssured.baseURI = BaseURI;
		RequestSpecification request = RestAssured.given();

		Response response = request.delete("/" + EmpId);
		return response;
	}
public Response GetMethod(int EmpId) {
		
		RestAssured.baseURI = BaseURI;
		RequestSpecification request = RestAssured.given();
		Response response = request.get("/" + EmpId);
		
		return response;
}
}



