package edu.eci.arsw.blueprints.filters;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UndersamplingFilterTest {

    private UndersamplingFilter filter;

    @BeforeEach
    void setUp() {
        filter = new UndersamplingFilter();
    }

    @Test
    void shouldKeepOnlyEvenIndexPoints() {
        // Arrange
        List<Point> points = Arrays.asList(
                new Point(0, 0), // índice 0 - mantener
                new Point(1, 1), // índice 1 - eliminar
                new Point(2, 2), // índice 2 - mantener
                new Point(3, 3), // índice 3 - eliminar
                new Point(4, 4)  // índice 4 - mantener
        );
        Blueprint blueprint = new Blueprint("author", "name", points);

        // Act
        Blueprint filtered = filter.apply(blueprint);

        // Assert
        assertEquals(3, filtered.getPoints().size());
        assertEquals(0, filtered.getPoints().get(0).getX());
        assertEquals(2, filtered.getPoints().get(1).getX());
        assertEquals(4, filtered.getPoints().get(2).getX());
    }

    @Test
    void shouldHandleBlueprintWithTwoOrLessPoints() {
        // Arrange
        List<Point> points = Arrays.asList(new Point(1, 1), new Point(2, 2));
        Blueprint blueprint = new Blueprint("author", "name", points);

        // Act
        Blueprint filtered = filter.apply(blueprint);

        // Assert
        assertEquals(2, filtered.getPoints().size());
        assertEquals(blueprint, filtered);
    }

    @Test
    void shouldHandleSinglePoint() {
        // Arrange
        List<Point> points = List.of(new Point(1, 1));
        Blueprint blueprint = new Blueprint("author", "name", points);

        // Act
        Blueprint filtered = filter.apply(blueprint);

        // Assert
        assertEquals(1, filtered.getPoints().size());
    }

    @Test
    void shouldHandleEmptyBlueprint() {
        // Arrange
        Blueprint blueprint = new Blueprint("author", "name", List.of());

        // Act
        Blueprint filtered = filter.apply(blueprint);

        // Assert
        assertTrue(filtered.getPoints().isEmpty());
    }

    @Test
    void shouldReducePointsByHalf() {
        // Arrange
        List<Point> points = Arrays.asList(
                new Point(0, 0), new Point(1, 1),
                new Point(2, 2), new Point(3, 3),
                new Point(4, 4), new Point(5, 5)
        );
        Blueprint blueprint = new Blueprint("author", "name", points);

        // Act
        Blueprint filtered = filter.apply(blueprint);

        // Assert
        assertEquals(3, filtered.getPoints().size()); // 6/2 = 3
    }

    @Test
    void shouldPreserveAuthorAndName() {
        // Arrange
        List<Point> points = Arrays.asList(
                new Point(0, 0), new Point(1, 1), new Point(2, 2)
        );
        Blueprint blueprint = new Blueprint("testAuthor", "testName", points);

        // Act
        Blueprint filtered = filter.apply(blueprint);

        // Assert
        assertEquals("testAuthor", filtered.getAuthor());
        assertEquals("testName", filtered.getName());
    }
}
