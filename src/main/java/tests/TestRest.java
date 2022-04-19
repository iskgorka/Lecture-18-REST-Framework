package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class TestRest {

    @Test
    public void testGetUser() {
        Response response = when()
                .get("https://reqres.in/api/users/{id}", 2);
        response.then().statusCode(200);

        System.out.println(response.asString());
    }

    @Test
    public void testGetUser1(){
        given()
                .get("https://reqres.in/api/users?page=2").then().statusCode(200)
                .body("data.id[1]",equalTo(8))
                .body("data.first_name", hasItems("Michael", "Lindsay"));
    }

    @Test
    public void testGetUser2() {
        String jsonSchema = "jsonSchema";

        Response response = when()
                .get("https://reqres.in/api/users/{id}", 2);
        response.then()
                .statusCode(200)
                .assertThat().body(matchesJsonSchemaInClasspath("schema_j.json"));
    }

    @Test
    public void testGetUser404() {
        Response response = when()
                .get("https://reqres.in/api/users/{id}", 23);
        response.then().statusCode(404);

        System.out.println(response.asString());
    }

    @Test
    public void testPost() {
        RestAssured.baseURI = "https://reqres.in/api/users";

        String sasha = "{\n" +
                "    \"name\": \"САША АУЕ 228\",\n" +
                "    \"job\": \"Гараж\"\n" +
                "}";

        RestAssured.given().body(sasha).post().then().log().all().assertThat().statusCode(201);
    }

    @Test
    public void testPost1(){
        JSONObject request = new JSONObject();

        request.put("name", "Ajeet");
        request.put("Job", "Teacher");
        
        given().
                header("Content-type","application/json").
                contentType(ContentType.JSON).
                body(request.toJSONString()).
                when().post("https://reqres.in/api/users").
                then().statusCode(201).log().all();
    }

}
