package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class RestfulBookerAllApiTest {

    String bookingId;

    @Test(priority = 1)
    public  void validatePostApi(){
        RestAssured.baseURI="https://restful-booker.herokuapp.com";

        String requestBody = """
              {
               "firstname" : "Shravya",
                  "lastname" : "Vignesh",
                  "totalprice" : 111,
                  "depositpaid" : true,
                  "bookingdates" : {
                      "checkin" : "2018-01-01",
                      "checkout" : "2019-01-01"
                  },
                  "additionalneeds" : "Breakfast"
              }
              """;

        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .body("booking.firstname",equalTo("Shravya"))
                .body("booking.lastname",equalTo("Vignesh"))
                .body("booking.depositpaid",equalTo(true))
//                .log().all()
                .extract()
                .response();

        bookingId=response.jsonPath().getString("bookingid");
    }

    @Test(priority = 2,dependsOnMethods = "validatePostApi")
    public  void validateGetApi(){
        RestAssured.baseURI="https://restful-booker.herokuapp.com";

        Response response = given()
                .header("Content-Type", "application/json")
                .when()
                .get("/booking/"+bookingId)
                .then()
                .statusCode(200)
                .body("firstname",equalTo("Shravya"))
                .body("depositpaid",equalTo(true))
//                .log().all()
                .extract()
                .response();
    }


    @Test(priority = 3,dependsOnMethods = "validatePostApi")
    public  void validatePutApi(){
        RestAssured.baseURI="https://restful-booker.herokuapp.com";
        String requestBody = """
              {
               "firstname" : "Shravya Suchithra",
                  "lastname" : "Vignesh",
                   "totalprice": 111,
                      "depositpaid": true,
                      "bookingdates": {
                          "checkin": "2018-01-01",
                          "checkout": "2019-01-01"
                      },
                      "additionalneeds": "Lunch"
              }
              """;

        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie","token="+getAuthToken())
                .body(requestBody)
                .when()
                .put("/booking/"+bookingId)
                .then()
                .statusCode(200)
                .body("firstname",equalTo("Shravya Suchithra"))
                .body("lastname",equalTo("Vignesh"))
                .body("depositpaid",equalTo(true))
//                .log().all()
                .extract()
                .response();
    }

    @Test(priority = 4,dependsOnMethods = "validatePostApi")
    public  void validateDeleteApi(){
        RestAssured.baseURI="https://restful-booker.herokuapp.com";

        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie","token="+getAuthToken())
                .when()
                .delete("/booking/"+bookingId)
                .then()
                .statusCode(201)
//                .log().all()
                .extract()
                .response();
    }


    public String getAuthToken(){
        RestAssured.baseURI="https://restful-booker.herokuapp.com";

        String requestBody = """
              {
                  "username" : "admin",
                  "password" : "password123"
              }
              """;

        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .body("token",notNullValue())
                .extract()
                .response();

        return response.jsonPath().getString("token");
    }

}
