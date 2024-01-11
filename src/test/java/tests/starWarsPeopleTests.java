package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import models.payloadBodyModel;
import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.allPeopleResponseModel;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class starWarsPeopleTests {

   RequestSpecification req  =new RequestSpecBuilder().setBaseUri("https://swapi-graphql.netlify.app")
           .setContentType(ContentType.JSON)
           .build();

   @Test
   public String getaAllPeople() throws JsonProcessingException {
      payloadBodyModel payload = new payloadBodyModel();
      payload.setQuery("query ExampleQuery {\n \n  allPeople {\n    people {\n      id\n      name\n    }\n  }\n}\n");
      Map<String, String> emptyMap =new HashMap<>();
      payload.setVariables(emptyMap);
      payload.setOperationName("ExampleQuery");

      allPeopleResponseModel response =given().spec(req).body(payload)
              .when().post("/.netlify/functions/index")
              .then().extract().response().as(allPeopleResponseModel.class);

      ObjectMapper objectMapper = new ObjectMapper();
      String jsonString = objectMapper.writeValueAsString(response.getData());
      System.out.println("JSON String: " + jsonString);

      Pattern pattern = Pattern.compile("\"id\":\\s*\"([^\"]+)\"");

      Matcher matcher = pattern.matcher(jsonString);
      List<String> ids = new ArrayList<>();

      // Find values near the "id" attribute using regular expression matching
      while (matcher.find()) {
         String idValue = matcher.group(1);
         ids.add(idValue);
      }

      return ids.get(0);

   }


   @Test
   public void getAPerson() throws JsonProcessingException {

      payloadBodyModel payload2 = new payloadBodyModel();
      payload2.setQuery("query ExampleQuery($personId: ID) {\n \n  person(id: $personId) {\n    name\n  }\n}\n");
      Map<String, String> variables = new HashMap<>();
      variables.put("personId", getaAllPeople()); // Assuming "personId" is the key and "cGVvcGxlOjE=" is the value
      payload2.setVariables(variables);
      payload2.setOperationName("ExampleQuery");


      String responseperson = given().spec(req).body(payload2)
              .when().post("/.netlify/functions/index")
              .then().extract().response().asString();


      JsonPath js =new JsonPath(responseperson);
      String name =js.getString("data.person.name");
      MatcherAssert.assertThat("Name is equal to 'Luke'", name, equalTo("Luke Skywalker"));

   }
}
