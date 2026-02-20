package edu.eci.arsw.blueprints.dto.request;

import edu.eci.arsw.blueprints.model.Point;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record NewBlueprintRequest(
        @NotBlank(message = "El autor no puede estar vacío")
        String author,
        
        @NotBlank(message = "El nombre no puede estar vacío")
        String name,
        
        @Valid
        List<Point> points
) { }
