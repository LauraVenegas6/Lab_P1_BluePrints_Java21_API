package edu.eci.arsw.blueprints.filters;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RedundancyFilterTest {

    private RedundancyFilter filter;

    @BeforeEach
    void setUp() {
        filter = new RedundancyFilter();
    }

    @Test
    void shouldRemoveConsecutiveDuplicatePoints() {

        List<Point> points = Arrays.asList(
                new Point(1, 1),
                new Point(1, 1), 
                new Point(2, 2),
                new Point(2, 2), 
                new Point(3, 3)
        );
        Blueprint blueprint = new Blueprint("author", "name", points);
        Blueprint filtered = filter.apply(blueprint);

        assertEquals(3, filtered.getPoints().size());
        assertEquals(1, filtered.getPoints().get(0).getX());
        assertEquals(1, filtered.getPoints().get(0).getY());
        assertEquals(2, filtered.getPoints().get(1).getX());
        assertEquals(2, filtered.getPoints().get(1).getY());
        assertEquals(3, filtered.getPoints().get(2).getX());
        assertEquals(3, filtered.getPoints().get(2).getY());
    }

    @Test
    void shouldHandleEmptyBlueprint() {
        Blueprint blueprint = new Blueprint("author", "name", List.of());
        Blueprint filtered = filter.apply(blueprint);
        assertTrue(filtered.getPoints().isEmpty());
    }

    @Test
    void shouldHandleSinglePoint() {
        List<Point> points = List.of(new Point(1, 1));
        Blueprint blueprint = new Blueprint("author", "name", points);
        Blueprint filtered = filter.apply(blueprint);
        assertEquals(1, filtered.getPoints().size());
    }

    @Test
    void shouldNotRemoveNonConsecutiveDuplicates() {
        
        List<Point> points = Arrays.asList(
                new Point(1, 1),
                new Point(2, 2),
                new Point(1, 1)
        );
        Blueprint blueprint = new Blueprint("author", "name", points);
        Blueprint filtered = filter.apply(blueprint);
        assertEquals(3, filtered.getPoints().size());
    }

    @Test
    void shouldPreserveAuthorAndName() {

        List<Point> points = List.of(new Point(1, 1), new Point(1, 1));
        Blueprint blueprint = new Blueprint("testAuthor", "testName", points);

        Blueprint filtered = filter.apply(blueprint);
        assertEquals("testAuthor", filtered.getAuthor());
        assertEquals("testName", filtered.getName());
    }
}
