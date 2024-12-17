package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GetApiTest {

   @Test
    public  void validateGetApi(){
       RestAssured.baseURI="https://restful-booker.herokuapp.com";

       Response response = given()
               .header("Content-Type", "application/json")
               .when()
               .get("/booking/470")
               .then()
               .statusCode(200)
               .body("firstname",equalTo("John"))
               .body("depositpaid",equalTo(true))
               .log().all()
               .extract()
               .response();
   }

   @Test
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
              .log().all()
              .extract()
              .response();
   }

}
