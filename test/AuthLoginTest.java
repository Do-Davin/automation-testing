import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Auth Login — Integration Tests (Live Render API)")
public class AuthLoginTest {

    private static final String BASE_URL = "https://automation-testing-g37b.onrender.com";
    private static HttpClient client;

    // Setup
    @BeforeAll
    static void setupClient() {
        client = HttpClient.newHttpClient();
        System.out.println("==> Target: " + BASE_URL);
    }

    @AfterAll
    static void done() {
        System.out.println("==> All tests finished.");
    }

    // Helpers
    private HttpResponse<String> post(String path, String json) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> get(String path) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .GET()
                .build();
        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }

    private String uniqueUser(String prefix) {
        return prefix + "_" + System.currentTimeMillis();
    }

    private void register(String username, String password) throws Exception {
        post("/auth/register",
                "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}");
    }

    // Test 1: API reachable
    @Test
    @DisplayName("T1: API root should be reachable")
    void t1_apiShouldBeReachable() throws Exception {
        HttpResponse<String> res = get("/");
        int status = res.statusCode();
        System.out.println("  GET / -> " + status);
        assertTrue(status >= 200 && status < 600,
                "Expected a valid HTTP status, got: " + status);
    }

    // Test 2: Register new user
    @Test
    @DisplayName("T2: POST /auth/register -> 201 Created")
    void t2_registerShouldReturn201() throws Exception {
        String user = uniqueUser("reg");
        String body = "{\"username\":\"" + user + "\",\"password\":\"Test@1234\"}";
        HttpResponse<String> res = post("/auth/register", body);
        System.out.println("  POST /auth/register -> " + res.statusCode() + " | " + res.body());
        assertEquals(201, res.statusCode(),
                "Expected 201 Created, got: " + res.statusCode() + " body: " + res.body());
    }

    // Test 3: Valid login returns token
    @Test
    @DisplayName("T3: POST /auth/login -> 200 + token in body")
    void t3_validLoginShouldReturnToken() throws Exception {
        String user = uniqueUser("login");
        register(user, "Test@1234");

        String body = "{\"username\":\"" + user + "\",\"password\":\"Test@1234\"}";
        HttpResponse<String> res = post("/auth/login", body);
        System.out.println("  POST /auth/login -> " + res.statusCode() + " | " + res.body());

        assertEquals(200, res.statusCode(),
                "Expected 200 OK, got: " + res.statusCode());
        assertTrue(
                res.body().contains("token") || res.body().contains("access_token"),
                "Expected token in response, got: " + res.body());
    }

    // Test 4: Wrong password -> 401
    @Test
    @DisplayName("T4: POST /auth/login -> 401 when password is wrong")
    void t4_wrongPasswordShouldReturn401() throws Exception {
        String user = uniqueUser("wp");
        register(user, "Correct@1");

        String body = "{\"username\":\"" + user + "\",\"password\":\"Wrong@999\"}";
        HttpResponse<String> res = post("/auth/login", body);
        System.out.println("  POST /auth/login (wrong pw) -> " + res.statusCode() + " | " + res.body());

        assertEquals(401, res.statusCode(),
                "Expected 401 Unauthorized, got: " + res.statusCode());
    }

    // Test 5: Unknown user -> 401
    @Test
    @DisplayName("T5: POST /auth/login -> 401 when user does not exist")
    void t5_unknownUserShouldReturn401() throws Exception {
        String body = "{\"username\":\"ghost_xyz_999\",\"password\":\"Whatever@1\"}";
        HttpResponse<String> res = post("/auth/login", body);
        System.out.println("  POST /auth/login (unknown) -> " + res.statusCode() + " | " + res.body());

        assertEquals(401, res.statusCode(),
                "Expected 401 Unauthorized, got: " + res.statusCode());
    }

    // Test 6: Empty body -> 400 or 401
    @Test
    @DisplayName("T6: POST /auth/login -> 400 or 401 when body is empty")
    void t6_emptyBodyShouldBeRejected() throws Exception {
        HttpResponse<String> res = post("/auth/login", "{}");
        System.out.println("  POST /auth/login (empty) -> " + res.statusCode() + " | " + res.body());

        int status = res.statusCode();
        assertTrue(status == 400 || status == 401,
                "Expected 400 or 401, got: " + status);
    }

    // Test 7: Response Content-Type is JSON
    @Test
    @DisplayName("T7: POST /auth/login -> Content-Type is application/json")
    void t7_loginResponseShouldBeJson() throws Exception {
        String user = uniqueUser("json");
        register(user, "Test@1234");

        String body = "{\"username\":\"" + user + "\",\"password\":\"Test@1234\"}";
        HttpResponse<String> res = post("/auth/login", body);

        String contentType = res.headers().firstValue("content-type").orElse("");
        System.out.println("  Content-Type: " + contentType);
        assertTrue(contentType.contains("application/json"),
                "Expected application/json, got: " + contentType);
    }
}
