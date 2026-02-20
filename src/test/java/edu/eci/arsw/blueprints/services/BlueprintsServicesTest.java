package edu.eci.arsw.blueprints.services;

import edu.eci.arsw.blueprints.filters.BlueprintsFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistence;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlueprintsServicesTest {

    @Mock
    private BlueprintPersistence persistence;

    @Mock
    private BlueprintsFilter filter;

    @InjectMocks
    private BlueprintsServices services;

    private Blueprint testBlueprint;

    @BeforeEach
    void setUp() {
        List<Point> points = Arrays.asList(
                new Point(1, 1),
                new Point(2, 2)
        );
        testBlueprint = new Blueprint("john", "house", points);
    }

    @Test
    void shouldAddNewBlueprint() throws BlueprintPersistenceException {
        // Arrange
        doNothing().when(persistence).saveBlueprint(any(Blueprint.class));

        // Act
        services.addNewBlueprint(testBlueprint);

        // Assert
        verify(persistence, times(1)).saveBlueprint(testBlueprint);
    }

    @Test
    void shouldThrowExceptionWhenAddingDuplicateBlueprint() throws BlueprintPersistenceException {
        // Arrange
        doThrow(new BlueprintPersistenceException("Blueprint already exists"))
                .when(persistence).saveBlueprint(any(Blueprint.class));

        // Act & Assert
        assertThrows(BlueprintPersistenceException.class, () -> {
            services.addNewBlueprint(testBlueprint);
        });
    }

    @Test
    void shouldGetAllBlueprintsWithFilter() {
        // Arrange
        Set<Blueprint> blueprints = new HashSet<>(Arrays.asList(testBlueprint));
        when(persistence.getAllBlueprints()).thenReturn(blueprints);
        when(filter.apply(any(Blueprint.class))).thenReturn(testBlueprint);

        // Act
        Set<Blueprint> result = services.getAllBlueprints();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(persistence, times(1)).getAllBlueprints();
        verify(filter, times(1)).apply(any(Blueprint.class));
    }

    @Test
    void shouldGetBlueprintsByAuthorWithFilter() throws BlueprintNotFoundException {
        // Arrange
        Set<Blueprint> blueprints = new HashSet<>(Arrays.asList(testBlueprint));
        when(persistence.getBlueprintsByAuthor("john")).thenReturn(blueprints);
        when(filter.apply(any(Blueprint.class))).thenReturn(testBlueprint);

        // Act
        Set<Blueprint> result = services.getBlueprintsByAuthor("john");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(persistence, times(1)).getBlueprintsByAuthor("john");
        verify(filter, times(1)).apply(any(Blueprint.class));
    }

    @Test
    void shouldThrowExceptionWhenAuthorNotFound() throws BlueprintNotFoundException {
        // Arrange
        when(persistence.getBlueprintsByAuthor("unknown"))
                .thenThrow(new BlueprintNotFoundException("No blueprints found for author: unknown"));

        // Act & Assert
        assertThrows(BlueprintNotFoundException.class, () -> {
            services.getBlueprintsByAuthor("unknown");
        });
    }

    @Test
    void shouldGetBlueprintWithFilter() throws BlueprintNotFoundException {
        // Arrange
        when(persistence.getBlueprint("john", "house")).thenReturn(testBlueprint);
        when(filter.apply(testBlueprint)).thenReturn(testBlueprint);

        // Act
        Blueprint result = services.getBlueprint("john", "house");

        // Assert
        assertNotNull(result);
        assertEquals("john", result.getAuthor());
        assertEquals("house", result.getName());
        verify(persistence, times(1)).getBlueprint("john", "house");
        verify(filter, times(1)).apply(testBlueprint);
    }

    @Test
    void shouldThrowExceptionWhenBlueprintNotFound() throws BlueprintNotFoundException {
        // Arrange
        when(persistence.getBlueprint("john", "unknown"))
                .thenThrow(new BlueprintNotFoundException("Blueprint not found"));

        // Act & Assert
        assertThrows(BlueprintNotFoundException.class, () -> {
            services.getBlueprint("john", "unknown");
        });
    }

    @Test
    void shouldAddPointToBlueprint() throws BlueprintNotFoundException {
        // Arrange
        doNothing().when(persistence).addPoint("john", "house", 3, 3);

        // Act
        services.addPoint("john", "house", 3, 3);

        // Assert
        verify(persistence, times(1)).addPoint("john", "house", 3, 3);
    }

    @Test
    void shouldThrowExceptionWhenAddingPointToBlueprintNotFound() throws BlueprintNotFoundException {
        // Arrange
        doThrow(new BlueprintNotFoundException("Blueprint not found"))
                .when(persistence).addPoint("unknown", "house", 3, 3);

        // Act & Assert
        assertThrows(BlueprintNotFoundException.class, () -> {
            services.addPoint("unknown", "house", 3, 3);
        });
    }
}
