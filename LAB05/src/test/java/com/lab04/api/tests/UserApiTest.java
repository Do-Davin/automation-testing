package com.lab04.api.tests;

import com.aventstack.extentreports.Status;
import com.lab04.api.config.ApiConfig;
import com.lab04.api.config.RequestSpecFactory;
import com.lab04.api.model.User;
import com.lab04.utils.ExtentReportManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class UserApiTest extends ApiBaseTest {

    @Test(description = "GET /users — returns a non-empty list and validates JSON schema")
    public void testGetAllUsers() {
        Response response = RestAssured.given()
                .spec(RequestSpecFactory.buildBaseSpec())
                .when()
                .get(ApiConfig.USERS_ENDPOINT)
                .then()
                .spec(RequestSpecFactory.build200Spec())
                .body(matchesJsonSchemaInClasspath("schemas/users-list-schema.json"))
                .extract().response();

        logResponse(response, "GET /users");

        List<User> users = response.jsonPath().getList("", User.class);
        Assert.assertFalse(users.isEmpty(), "Users list should not be empty");
        Assert.assertEquals(users.size(), 10, "JSONPlaceholder should return 10 users");
        ExtentReportManager.getCurrentTest().log(Status.PASS,
                "Returned " + users.size() + " users — schema validated");
    }

    @Test(description = "GET /users/{id} — returns a single user with correct fields and schema")
    public void testGetUserById() {
        int userId = 1;
        Response response = RestAssured.given()
                .spec(RequestSpecFactory.buildBaseSpec())
                .pathParam("id", userId)
                .when()
                .get(ApiConfig.USERS_ENDPOINT + "/{id}")
                .then()
                .spec(RequestSpecFactory.build200Spec())
                .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"))
                .body("id", equalTo(userId))
                .body("name", not(emptyString()))
                .body("email", containsString("@"))
                .extract().response();

        logResponse(response, "GET /users/1");

        User user = response.as(User.class);
        Assert.assertEquals(user.getId(), userId);
        Assert.assertNotNull(user.getName());
        Assert.assertNotNull(user.getEmail());
        ExtentReportManager.getCurrentTest().log(Status.PASS,
                "User retrieved: " + user);
    }

    @Test(description = "GET /users/{id} — non-existent user returns HTTP 404")
    public void testGetNonExistentUser() {
        Response response = RestAssured.given()
                .spec(RequestSpecFactory.buildBaseSpec())
                .pathParam("id", 9999)
                .when()
                .get(ApiConfig.USERS_ENDPOINT + "/{id}")
                .then()
                .statusCode(404)
                .extract().response();

        logResponse(response, "GET /users/9999");
        ExtentReportManager.getCurrentTest().log(Status.PASS,
                "HTTP 404 correctly returned for non-existent user ID 9999");
    }

    @Test(description = "GET /users — filter by username query param returns matching user")
    public void testGetUserByUsername() {
        String username = "Bret";
        Response response = RestAssured.given()
                .spec(RequestSpecFactory.buildBaseSpec())
                .queryParam("username", username)
                .when()
                .get(ApiConfig.USERS_ENDPOINT)
                .then()
                .spec(RequestSpecFactory.build200Spec())
                .body("[0].username", equalTo(username))
                .extract().response();

        logResponse(response, "GET /users?username=Bret");

        List<User> users = response.jsonPath().getList("", User.class);
        Assert.assertEquals(users.size(), 1, "Should return exactly one matching user");
        Assert.assertEquals(users.get(0).getUsername(), username);
        ExtentReportManager.getCurrentTest().log(Status.PASS,
                "Filtered user found: " + users.get(0));
    }

    @Test(description = "GET /users/{id}/posts — returns posts belonging to the given user")
    public void testGetPostsByUser() {
        int userId = 1;
        Response response = RestAssured.given()
                .spec(RequestSpecFactory.buildBaseSpec())
                .pathParam("id", userId)
                .when()
                .get(ApiConfig.USERS_ENDPOINT + "/{id}/posts")
                .then()
                .spec(RequestSpecFactory.build200Spec())
                .body("$", not(empty()))
                .body("userId", everyItem(equalTo(userId)))
                .extract().response();

        logResponse(response, "GET /users/1/posts");

        int count = response.jsonPath().getList("").size();
        ExtentReportManager.getCurrentTest().log(Status.PASS,
                "User " + userId + " has " + count + " posts — all have correct userId");
    }
}
