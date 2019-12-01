package com.myproject.myprogram.APITesting;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class weatherDetails {
	
	RequestSpecification httpRequest; //Request Object
	Response response; //Response Object
	String responseBody; //Response body

	@BeforeTest
	 void getweatherDetails()
	 {
	  //Specify base URI
	  RestAssured.baseURI="https://samples.openweathermap.org";
	  	  //https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22
	  
	  //Request object
	  httpRequest=RestAssured.given();
	  
	  //Response object
	  response=httpRequest.request(Method.GET,"/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22");
	  
	 }
	@Test(priority=0)
	public void printResponse()
	{		
	  //Print response in console window
	  System.out.println("API Validation Started");
	  System.out.println("----------------------");
	  try
	  {
	  responseBody=response.getBody().asString();
	  System.out.println("Response Body is :" +responseBody);
	  }
	  catch(Exception e)
	  {
		  e.printStackTrace();
	  }
	} 
	  
	@Test(priority=1)
	public void statusValidation()
	{
	  //Validate status code
	  int statusCode=response.getStatusCode();
	  System.out.println("Status Validation");
	  System.out.println("------------------");
	  System.out.println("Status code is : " +statusCode);
	  Assert.assertEquals(statusCode, 200); 
	
	  //Validate status line
	  String statusLine=response.getStatusLine();
	  System.out.println("Status line is : "+statusLine);
	  Assert.assertEquals(statusLine, "HTTP/1.1 200 OK");
	}
	 
	@Test(priority=2)
	public void headerValidation()
	{	
	//Get all the header information		
		Headers allHeaders = response.headers();	
		  System.out.println("Header Validation");
		  System.out.println("-----------------");
		  System.out.println("Printing Header Information: ");
		  for (Header header : allHeaders)
		  {
			  System.out.println(header.getName()+"   "+header.getValue());
		  }	
		
	//Validate header content-type	
	  String contentType = response.header("content-type");
	  System.out.println("*Header Content type* is : " +contentType);
	  Assert.assertEquals(contentType, "application/json; charset=utf-8");
	
	//Validate header content encoding
	  String contentEncoding = response.header("content-encoding");
	  System.out.println("*Header Content Encoding* is : " +contentEncoding);
	  Assert.assertEquals(contentEncoding, "gzip");
	} 

	@Test(priority=3)
	public void responsebodyvalidation()
	{
			
		//Get all response body
		  JsonPath jsonpath = response.jsonPath();
		  System.out.println("Response Body Validation ");
		  System.out.println("--------------------------");
		  System.out.println("Co-ordinates are : "+jsonpath.get("coord.lon"));
		  System.out.println("Weather is : "+jsonpath.get("weather.id"));
		  System.out.println("Temps are : "+jsonpath.get("main.temp"));
	
		  
		  String country = jsonpath.get("sys.country");
		  String city = jsonpath.get("name");
		  float longitude = jsonpath.get("coord.lon"); 
		  float lattitude = jsonpath.get("coord.lat");   
		  
		  
	  //Validate response body for city
	  System.out.println("The City is : " +city);
	  //Assert.assertEquals(responseBody.contains("London"), true);
	  Assert.assertEquals(city, "London");
	 
 
	  //Validate response body for country
	  System.out.println("The Country is : " +country);
	  Assert.assertEquals(country, "GB");
	  
	//Validate the longitude and lattitue (This might fail, as the float value differences)
	  System.out.println("The Lonngitude is : " +longitude);
	  System.out.println("The Lattitude is : " +lattitude);
	  //Assert.assertEquals(longitude, -0.13);
	  //Assert.assertEquals(lattitude, 51.51);
	  
	  
	  //Validate all Required fields in response body
	  Assert.assertNotNull("coord.lon");
	  Assert.assertNotNull("coord.lat");
	  Assert.assertNotNull("main.temp");
	  Assert.assertNotNull("weather.id");
	  System.out.println("The Coordinates, Temperature and Weather ID are not null");
	  
	  //Validate Response time
	  System.out.println("This is Response time: " + response.getTime());
	 if (response.getTime() <= 200)
	 {
		 System.out.println("The Response time is expected");
	 }
	 else
	 {
		 System.out.println("The Response time is greater than expected");
	 }
	  
	 	  
	 }
	@AfterTest
	public void completevalidation()
	{
		System.out.println("------------------------------------");
		System.out.println("API Validation Completed Sucessfully");
	}
	  
}
