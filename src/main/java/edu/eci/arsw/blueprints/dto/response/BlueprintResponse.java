package edu.eci.arsw.blueprints.dto.response;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;

import java.util.List;

public record BlueprintResponse(
        Long id,
        String author,
        String name,
        List<Point> points,
        int pointsCount
) {
    public static BlueprintResponse fromBlueprint(Blueprint blueprint) {
        return new BlueprintResponse(
                blueprint.getId(),
                blueprint.getAuthor(),
                blueprint.getName(),
                blueprint.getPoints(),
                blueprint.getPoints() != null ? blueprint.getPoints().size() : 0
        );
    }
}
