package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.dto.request.AddPointRequest;
import edu.eci.arsw.blueprints.dto.request.NewBlueprintRequest;
import edu.eci.arsw.blueprints.dto.response.BlueprintResponse;
import edu.eci.arsw.blueprints.dto.response.MessageResponse;
import edu.eci.arsw.blueprints.model.ApiResponse;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/blueprints")
@Tag(name = "blueprints-api-controller", description = "API para gestión de blueprints y sus puntos")
public class BlueprintsAPIController {

    private final BlueprintsServices services;

    public BlueprintsAPIController(BlueprintsServices services) { this.services = services; }

    // GET /blueprints
    @GetMapping
    @Operation(summary = "Obtener todos los blueprints", description = "Retorna la lista completa de blueprints almacenados")
    public ResponseEntity<ApiResponse<Set<BlueprintResponse>>> getAll() {
        Set<BlueprintResponse> responses = services.getAllBlueprints().stream()
                .map(BlueprintResponse::fromBlueprint)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(ApiResponse.ok(responses));
    }

    // GET /blueprints/{author}
    @GetMapping("/{author}")
    @Operation(summary = "Obtener blueprints por autor", description = "Retorna todos los blueprints creados por un autor específico")
    public ResponseEntity<ApiResponse<Set<BlueprintResponse>>> byAuthor(@PathVariable String author) {
        try {
            Set<BlueprintResponse> responses = services.getBlueprintsByAuthor(author).stream()
                    .map(BlueprintResponse::fromBlueprint)
                    .collect(Collectors.toSet());
            return ResponseEntity.ok(ApiResponse.ok(responses));
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.notFound(e.getMessage()));
        }
    }

    // GET /blueprints/{author}/{bpname}
    @GetMapping("/{author}/{bpname}")
    @Operation(summary = "Obtener blueprint específico", description = "Retorna un blueprint específico dado el autor y nombre")
    public ResponseEntity<ApiResponse<BlueprintResponse>> byAuthorAndName(@PathVariable String author, @PathVariable String bpname) {
        try {
            Blueprint blueprint = services.getBlueprint(author, bpname);
            return ResponseEntity.ok(ApiResponse.ok(BlueprintResponse.fromBlueprint(blueprint)));
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.notFound(e.getMessage()));
        }
    }

    // POST /blueprints
    @PostMapping
    @Operation(summary = "Crear nuevo blueprint", description = "Crea y almacena un nuevo blueprint con sus puntos")
    public ResponseEntity<ApiResponse<BlueprintResponse>> add(@Valid @RequestBody NewBlueprintRequest req) {
        try {
            Blueprint bp = new Blueprint(req.author(), req.name(), req.points());
            services.addNewBlueprint(bp);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.created(BlueprintResponse.fromBlueprint(bp)));
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.forbidden(e.getMessage()));
        }
    }

    // PUT /blueprints/{author}/{bpname}/points
    @PutMapping("/{author}/{bpname}/points")
    @Operation(summary = "Agregar punto a blueprint", description = "Agrega un nuevo punto a un blueprint existente")
    public ResponseEntity<ApiResponse<MessageResponse>> addPoint(@PathVariable String author, 
                                                                   @PathVariable String bpname,
                                                                   @Valid @RequestBody AddPointRequest req) {
        try {
            services.addPoint(author, bpname, req.x(), req.y());
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(ApiResponse.accepted(MessageResponse.of("Point added successfully")));
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.notFound(e.getMessage()));
        }
    }
}
