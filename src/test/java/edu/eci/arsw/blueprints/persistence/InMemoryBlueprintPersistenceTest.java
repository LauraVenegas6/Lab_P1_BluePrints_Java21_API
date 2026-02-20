package edu.eci.arsw.blueprints.persistence;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryBlueprintPersistenceTest {

    private InMemoryBlueprintPersistence persistence;

    @BeforeEach
    void setUp() {
        // Cada test usa una nueva instancia con datos de ejemplo precargados
        persistence = new InMemoryBlueprintPersistence();
        // La instancia ya tiene: john/house, john/garage, jane/garden
    }

    @Test
    void shouldSaveBlueprint() throws BlueprintPersistenceException, BlueprintNotFoundException {
        // Arrange
        List<Point> points = List.of(new Point(5, 5));
        Blueprint newBlueprint = new Blueprint("alice", "garden", points);

        // Act
        persistence.saveBlueprint(newBlueprint);
        Blueprint retrieved = persistence.getBlueprint("alice", "garden");

        // Assert
        assertNotNull(retrieved);
        assertEquals("alice", retrieved.getAuthor());
        assertEquals("garden", retrieved.getName());
    }

    @Test
    void shouldThrowExceptionWhenSavingDuplicateBlueprint() {
        // Arrange - john/house ya existe en datos de ejemplo
        List<Point> points = List.of(new Point(1, 1));
        Blueprint duplicate = new Blueprint("john", "house", points);

        // Act & Assert
        assertThrows(BlueprintPersistenceException.class, () -> {
            persistence.saveBlueprint(duplicate);
        });
    }

    @Test
    void shouldGetBlueprintByAuthorAndName() throws BlueprintNotFoundException {
        // Act - john/house ya existe en datos de ejemplo
        Blueprint retrieved = persistence.getBlueprint("john", "house");

        // Assert
        assertNotNull(retrieved);
        assertEquals("john", retrieved.getAuthor());
        assertEquals("house", retrieved.getName());
        assertFalse(retrieved.getPoints().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenBlueprintNotFound() {
        // Act & Assert
        assertThrows(BlueprintNotFoundException.class, () -> {
            persistence.getBlueprint("unknown", "building");
        });
    }

    @Test
    void shouldGetBlueprintsByAuthor() throws BlueprintNotFoundException {
        // Act - john tiene house y garage en datos de ejemplo
        Set<Blueprint> blueprints = persistence.getBlueprintsByAuthor("john");

        // Assert
        assertNotNull(blueprints);
        assertEquals(2, blueprints.size());
    }

    @Test
    void shouldThrowExceptionWhenAuthorHasNoBlueprints() {
        // Act & Assert
        assertThrows(BlueprintNotFoundException.class, () -> {
            persistence.getBlueprintsByAuthor("unknown");
        });
    }

    @Test
    void shouldGetAllBlueprints() {
        // Act
        Set<Blueprint> allBlueprints = persistence.getAllBlueprints();

        // Assert
        assertNotNull(allBlueprints);
        assertEquals(3, allBlueprints.size()); // john/house, john/garage, jane/garden
    }

    @Test
    void shouldAddPointToBlueprint() throws BlueprintNotFoundException {
        // Arrange - john/house ya existe
        Blueprint before = persistence.getBlueprint("john", "house");
        int originalSize = before.getPoints().size();

        // Act
        persistence.addPoint("john", "house", 10, 10);
        Blueprint after = persistence.getBlueprint("john", "house");

        // Assert
        assertEquals(originalSize + 1, after.getPoints().size());
        Point lastPoint = after.getPoints().get(after.getPoints().size() - 1);
        assertEquals(10, lastPoint.getX());
        assertEquals(10, lastPoint.getY());
    }

    @Test
    void shouldThrowExceptionWhenAddingPointToBlueprintNotFound() {
        // Act & Assert
        assertThrows(BlueprintNotFoundException.class, () -> {
            persistence.addPoint("unknown", "building", 5, 5);
        });
    }
}
