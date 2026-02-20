package edu.eci.arsw.blueprints.filters;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IdentityFilterTest {

    private IdentityFilter filter;

    @BeforeEach
    void setUp() {
        filter = new IdentityFilter();
    }

    @Test
    void shouldReturnBlueprintUnchanged() {
        // Arrange
        List<Point> points = Arrays.asList(
                new Point(1, 1),
                new Point(2, 2),
                new Point(3, 3)
        );
        Blueprint blueprint = new Blueprint("author", "name", points);

        // Act
        Blueprint filtered = filter.apply(blueprint);

        // Assert
        assertSame(blueprint, filtered);
        assertEquals(3, filtered.getPoints().size());
    }

    @Test
    void shouldReturnSameInstanceWithEmptyPoints() {
        // Arrange
        Blueprint blueprint = new Blueprint("author", "name", List.of());

        // Act
        Blueprint filtered = filter.apply(blueprint);

        // Assert
        assertSame(blueprint, filtered);
        assertTrue(filtered.getPoints().isEmpty());
    }

    @Test
    void shouldNotModifyOriginalBlueprint() {
        // Arrange
        List<Point> points = Arrays.asList(
                new Point(1, 1),
                new Point(1, 1),
                new Point(2, 2)
        );
        Blueprint blueprint = new Blueprint("author", "name", points);
        int originalSize = blueprint.getPoints().size();

        // Act
        Blueprint filtered = filter.apply(blueprint);

        // Assert
        assertEquals(originalSize, filtered.getPoints().size());
        assertEquals(blueprint.getAuthor(), filtered.getAuthor());
        assertEquals(blueprint.getName(), filtered.getName());
    }
}
