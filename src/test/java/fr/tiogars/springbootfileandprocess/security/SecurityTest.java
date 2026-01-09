package fr.tiogars.springbootfileandprocess.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Security tests to verify authentication and authorization requirements.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityTest {

    /**
     * Random port assigned to the test server.
     */
    @LocalServerPort
    private int port;

    /**
     * REST template for making HTTP requests.
     */
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Get base URL for the test server.
     *
     * @return base URL
     */
    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    /**
     * Test that unauthenticated access to /process/execute is blocked.
     */
    @Test
    public void testProcessExecuteRequiresAuthentication() {
        String requestBody = """
            {
              "command": "echo",
              "workingDirectory": "/tmp",
              "arguments": ["test"]
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(
                    getBaseUrl() + "/process/execute", request, String.class);
            fail("Expected HttpClientErrorException to be thrown");
        } catch (HttpClientErrorException e) {
            // Should return 401 Unauthorized
            assertEquals(HttpStatus.UNAUTHORIZED, e.getStatusCode());
        }
    }

    /**
     * Test that authenticated access to /process/execute is allowed.
     */
    @Test
    public void testProcessExecuteWithAuthentication() {
        String requestBody = """
            {
              "command": "echo",
              "workingDirectory": "/tmp",
              "arguments": ["test"]
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " +
                Base64.getEncoder().encodeToString("admin:admin".getBytes()));
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                getBaseUrl() + "/process/execute", request, String.class);

        // Should return 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test that public endpoint /system/hostname is accessible without authentication.
     */
    @Test
    public void testSystemHostnameIsPublic() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                getBaseUrl() + "/system/hostname", String.class);

        // Should return 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test that Swagger UI is accessible without authentication.
     */
    @Test
    public void testSwaggerUiIsPublic() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                getBaseUrl() + "/swagger-ui/index.html", String.class);

        // Should return 200 OK (redirect may happen, so we accept 3xx or 200)
        assertTrue(response.getStatusCode().is2xxSuccessful() ||
                response.getStatusCode().is3xxRedirection());
    }

    /**
     * Test that API docs are accessible without authentication.
     */
    @Test
    public void testApiDocsIsPublic() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                getBaseUrl() + "/v3/api-docs", String.class);

        // Should return 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test that actuator health endpoint is public.
     */
    @Test
    public void testActuatorHealthIsPublic() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                getBaseUrl() + "/actuator/health", String.class);

        // Should return 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test that other actuator endpoints require authentication.
     */
    @Test
    public void testActuatorEndpointsRequireAuthentication() {
        try {
            restTemplate.getForEntity(
                    getBaseUrl() + "/actuator/info", String.class);
            fail("Expected HttpClientErrorException to be thrown");
        } catch (HttpClientErrorException e) {
            // Should return 401 Unauthorized
            assertEquals(HttpStatus.UNAUTHORIZED, e.getStatusCode());
        }
    }

    /**
     * Test that /file/** endpoints are public.
     */
    @Test
    public void testFileEndpointsArePublic() {
        // The /file/list endpoint is accessible, even if it may error due to invalid parameters
        // We just want to verify it doesn't return 401 Unauthorized
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    getBaseUrl() + "/file/list?directoryParam=/tmp", String.class);
            // Should return 200 OK or other status, but NOT 401
            assertTrue(response.getStatusCode() != HttpStatus.UNAUTHORIZED);
        } catch (HttpClientErrorException | org.springframework.web.client.HttpServerErrorException e) {
            // If we get an HTTP error, it should NOT be 401 Unauthorized
            assertTrue(e.getStatusCode() != HttpStatus.UNAUTHORIZED,
                    "File endpoint should not require authentication but got: " + e.getStatusCode());
        }
    }
}
