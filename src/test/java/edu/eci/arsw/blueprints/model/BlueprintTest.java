package edu.eci.arsw.blueprints.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlueprintTest {

    private Blueprint blueprint;
    private List<Point> points;

    @BeforeEach
    void setUp() {
        points = new ArrayList<>(Arrays.asList(
                new Point(1, 1),
                new Point(2, 2),
                new Point(3, 3)
        ));
        blueprint = new Blueprint("john", "house", points);
    }

    @Test
    void shouldCreateBlueprintWithAuthorNameAndPoints() {
        // Assert
        assertNotNull(blueprint);
        assertEquals("john", blueprint.getAuthor());
        assertEquals("house", blueprint.getName());
        assertEquals(3, blueprint.getPoints().size());
    }

    @Test
    void shouldCreateEmptyBlueprintWithDefaultConstructor() {
        // Act
        Blueprint emptyBlueprint = new Blueprint();

        // Assert
        assertNotNull(emptyBlueprint);
        assertNull(emptyBlueprint.getAuthor());
        assertNull(emptyBlueprint.getName());
        assertNotNull(emptyBlueprint.getPoints());
    }

    @Test
    void shouldAddPointToBlueprint() {
        // Arrange
        Point newPoint = new Point(4, 4);

        // Act
        blueprint.addPoint(newPoint);

        // Assert
        assertEquals(4, blueprint.getPoints().size());
        assertEquals(newPoint, blueprint.getPoints().get(3));
    }

    @Test
    void shouldSetAndGetAuthor() {
        // Act
        blueprint.setAuthor("alice");

        // Assert
        assertEquals("alice", blueprint.getAuthor());
    }

    @Test
    void shouldSetAndGetName() {
        // Act
        blueprint.setName("garden");

        // Assert
        assertEquals("garden", blueprint.getName());
    }

    @Test
    void shouldSetAndGetPoints() {
        // Arrange
        List<Point> newPoints = Arrays.asList(new Point(5, 5), new Point(6, 6));

        // Act
        blueprint.setPoints(newPoints);

        // Assert
        assertEquals(2, blueprint.getPoints().size());
        assertEquals(newPoints, blueprint.getPoints());
    }

    @Test
    void shouldHandleNullPointsList() {
        // Act
        Blueprint blueprintWithNull = new Blueprint("author", "name", null);

        // Assert
        assertNotNull(blueprintWithNull.getPoints());
        assertTrue(blueprintWithNull.getPoints().isEmpty());
    }

    @Test
    void shouldCompareBlueprints() {
        // Arrange
        Blueprint blueprint1 = new Blueprint("john", "house", points);
        Blueprint blueprint2 = new Blueprint("john", "house", points);
        Blueprint blueprint3 = new Blueprint("alice", "garden", points);

        // Assert
        assertEquals(blueprint1, blueprint2);
        assertNotEquals(blueprint1, blueprint3);
    }

    @Test
    void shouldGenerateConsistentHashCode() {
        // Arrange
        Blueprint blueprint1 = new Blueprint("john", "house", points);
        Blueprint blueprint2 = new Blueprint("john", "house", points);

        // Assert
        assertEquals(blueprint1.hashCode(), blueprint2.hashCode());
    }
}
