package edu.eci.arsw.blueprints.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void shouldCreateOkResponse() {
        // Arrange
        String data = "test data";

        // Act
        ApiResponse<String> response = ApiResponse.ok(data);

        // Assert
        assertEquals(200, response.code());
        assertEquals("execute ok", response.message());
        assertEquals(data, response.data());
    }

    @Test
    void shouldCreateCreatedResponse() {
        // Arrange
        String data = "new resource";

        // Act
        ApiResponse<String> response = ApiResponse.created(data);

        // Assert
        assertEquals(201, response.code());
        assertEquals("resource created", response.message());
        assertEquals(data, response.data());
    }

    @Test
    void shouldCreateNotFoundResponse() {
        // Arrange
        String message = "Resource not found";

        // Act
        ApiResponse<String> response = ApiResponse.notFound(message);

        // Assert
        assertEquals(404, response.code());
        assertEquals(message, response.message());
        assertNull(response.data());
    }

    @Test
    void shouldCreateForbiddenResponse() {
        // Arrange
        String message = "Access forbidden";

        // Act
        ApiResponse<String> response = ApiResponse.forbidden(message);

        // Assert
        assertEquals(403, response.code());
        assertEquals(message, response.message());
        assertNull(response.data());
    }

    @Test
    void shouldCreateAcceptedResponse() {
        // Arrange
        String data = "accepted data";

        // Act
        ApiResponse<String> response = ApiResponse.accepted(data);

        // Assert
        assertEquals(202, response.code());
        assertEquals("accepted", response.message());
        assertEquals(data, response.data());
    }

    @Test
    void shouldWorkWithDifferentDataTypes() {
        // Act
        ApiResponse<Integer> intResponse = ApiResponse.ok(42);
        ApiResponse<Boolean> boolResponse = ApiResponse.ok(true);

        // Assert
        assertEquals(42, intResponse.data());
        assertEquals(true, boolResponse.data());
    }
}
