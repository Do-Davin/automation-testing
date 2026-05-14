package com.lab04.api.tests;

import com.aventstack.extentreports.Status;
import com.lab04.api.config.ApiConfig;
import com.lab04.api.config.RequestSpecFactory;
import com.lab04.api.model.Post;
import com.lab04.utils.ExtentReportManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class PostApiTest extends ApiBaseTest {

    // ------------------------------------------------------------------
    // GET tests
    // ------------------------------------------------------------------

    @Test(description = "GET /posts — returns 100 posts and validates list schema")
    public void testGetAllPosts() {
        Response response = RestAssured.given()
                .spec(RequestSpecFactory.buildBaseSpec())
                .when()
                .get(ApiConfig.POSTS_ENDPOINT)
                .then()
                .spec(RequestSpecFactory.build200Spec())
                .body(matchesJsonSchemaInClasspath("schemas/posts-list-schema.json"))
                .extract().response();

        logResponse(response, "GET /posts");

        List<Post> posts = response.jsonPath().getList("", Post.class);
        Assert.assertEquals(posts.size(), 100, "Should return 100 posts");
        ExtentReportManager.getCurrentTest().log(Status.PASS,
                "Retrieved " + posts.size() + " posts — schema validated");
    }

    @Test(description = "GET /posts/{id} — returns a single post with correct schema")
    public void testGetPostById() {
        int postId = 1;
        Response response = RestAssured.given()
                .spec(RequestSpecFactory.buildBaseSpec())
                .pathParam("id", postId)
                .when()
                .get(ApiConfig.POSTS_ENDPOINT + "/{id}")
                .then()
                .spec(RequestSpecFactory.build200Spec())
                .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"))
                .body("id", equalTo(postId))
                .body("title", not(emptyString()))
                .body("body", not(emptyString()))
                .extract().response();

        logResponse(response, "GET /posts/1");

        Post post = response.as(Post.class);
        Assert.assertEquals(post.getId(), postId);
        ExtentReportManager.getCurrentTest().log(Status.PASS,
                "Post retrieved: " + post);
    }

    @Test(description = "GET /posts — filter by userId returns only that user's posts")
    public void testGetPostsByUserId() {
        int userId = 1;
        Response response = RestAssured.given()
                .spec(RequestSpecFactory.buildBaseSpec())
                .queryParam("userId", userId)
                .when()
                .get(ApiConfig.POSTS_ENDPOINT)
                .then()
                .spec(RequestSpecFactory.build200Spec())
                .body("userId", everyItem(equalTo(userId)))
                .extract().response();

        logResponse(response, "GET /posts?userId=1");

        List<Post> posts = response.jsonPath().getList("", Post.class);
        Assert.assertEquals(posts.size(), 10, "User 1 should have 10 posts");
        ExtentReportManager.getCurrentTest().log(Status.PASS,
                "All " + posts.size() + " posts belong to userId=" + userId);
    }

    // ------------------------------------------------------------------
    // POST — Create
    // ------------------------------------------------------------------

    @Test(description = "POST /posts — create new post returns 201 with echoed body")
    public void testCreatePost() {
        Post newPost = new Post(1, "Automated Test Post", "This post was created by REST-assured.");

        Response response = RestAssured.given()
                .spec(RequestSpecFactory.buildBaseSpec())
                .body(newPost)
                .when()
                .post(ApiConfig.POSTS_ENDPOINT)
                .then()
                .spec(RequestSpecFactory.build201Spec())
                .body("title", equalTo(newPost.getTitle()))
                .body("body", equalTo(newPost.getBody()))
                .body("userId", equalTo(newPost.getUserId()))
                .body("id", notNullValue())
                .extract().response();

        logResponse(response, "POST /posts");

        Post created = response.as(Post.class);
        Assert.assertTrue(created.getId() > 0, "Created post should have a generated id");
        Assert.assertEquals(created.getTitle(), newPost.getTitle());
        ExtentReportManager.getCurrentTest().log(Status.PASS,
                "Post created with id=" + created.getId());
    }

    // ------------------------------------------------------------------
    // PUT — Full update
    // ------------------------------------------------------------------

    @Test(description = "PUT /posts/{id} — full update returns 200 with updated fields")
    public void testUpdatePost() {
        Post updatedPost = new Post(1, "Updated Title", "Updated body content.");
        updatedPost.setId(1);

        Response response = RestAssured.given()
                .spec(RequestSpecFactory.buildBaseSpec())
                .pathParam("id", 1)
                .body(updatedPost)
                .when()
                .put(ApiConfig.POSTS_ENDPOINT + "/{id}")
                .then()
                .spec(RequestSpecFactory.build200Spec())
                .body("id", equalTo(1))
                .body("title", equalTo("Updated Title"))
                .extract().response();

        logResponse(response, "PUT /posts/1");

        Post result = response.as(Post.class);
        Assert.assertEquals(result.getTitle(), "Updated Title");
        ExtentReportManager.getCurrentTest().log(Status.PASS,
                "Post updated: " + result);
    }

    // ------------------------------------------------------------------
    // PATCH — Partial update
    // ------------------------------------------------------------------

    @Test(description = "PATCH /posts/{id} — partial update changes only the title")
    public void testPatchPost() {
        String patchBody = "{\"title\": \"Patched Title\"}";

        Response response = RestAssured.given()
                .spec(RequestSpecFactory.buildBaseSpec())
                .pathParam("id", 1)
                .body(patchBody)
                .when()
                .patch(ApiConfig.POSTS_ENDPOINT + "/{id}")
                .then()
                .spec(RequestSpecFactory.build200Spec())
                .body("id", equalTo(1))
                .body("title", equalTo("Patched Title"))
                .extract().response();

        logResponse(response, "PATCH /posts/1");
        ExtentReportManager.getCurrentTest().log(Status.PASS,
                "Post title patched to: " + response.jsonPath().getString("title"));
    }

    // ------------------------------------------------------------------
    // DELETE
    // ------------------------------------------------------------------

    @Test(description = "DELETE /posts/{id} — returns 200 and empty body")
    public void testDeletePost() {
        Response response = RestAssured.given()
                .spec(RequestSpecFactory.buildBaseSpec())
                .pathParam("id", 1)
                .when()
                .delete(ApiConfig.POSTS_ENDPOINT + "/{id}")
                .then()
                .statusCode(200)
                .extract().response();

        logResponse(response, "DELETE /posts/1");

        // JSONPlaceholder returns {} on delete
        String body = response.getBody().asString().trim();
        Assert.assertTrue(body.equals("{}") || body.isEmpty(),
                "Delete response body should be empty or {}");
        ExtentReportManager.getCurrentTest().log(Status.PASS,
                "Post deleted — response body: " + body);
    }

    // ------------------------------------------------------------------
    // Response headers
    // ------------------------------------------------------------------

    @Test(description = "GET /posts/{id} — response Content-Type header is application/json")
    public void testResponseHeaders() {
        Response response = RestAssured.given()
                .spec(RequestSpecFactory.buildBaseSpec())
                .pathParam("id", 1)
                .when()
                .get(ApiConfig.POSTS_ENDPOINT + "/{id}")
                .then()
                .statusCode(200)
                .header("Content-Type", containsString("application/json"))
                .extract().response();

        logResponse(response, "GET /posts/1 — headers check");
        ExtentReportManager.getCurrentTest().log(Status.PASS,
                "Content-Type header: " + response.getHeader("Content-Type"));
    }
}
