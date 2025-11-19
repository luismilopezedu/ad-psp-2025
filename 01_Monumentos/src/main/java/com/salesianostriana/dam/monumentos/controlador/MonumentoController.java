package com.salesianostriana.dam.monumentos.controlador;

import com.salesianostriana.dam.monumentos.modelo.CrearMonumentoCmd;
import com.salesianostriana.dam.monumentos.modelo.Monumento;
import com.salesianostriana.dam.monumentos.modelo.MonumentoResponse;
import com.salesianostriana.dam.monumentos.servicio.MonumentoServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/monumento/")
@RequiredArgsConstructor
@Tag(name = "Monumento", description = "El controlador de monumentos, para poder realizar todas las operaciones de gestión")
public class MonumentoController {

    private final MonumentoServicio servicio;

    @GetMapping
    @Operation(summary = "Endpoint para obtener todos los monumentos almacenados")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Se han encontrado monumentos",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MonumentoResponse.class)),
                            examples = @ExampleObject(value = """
                                    [
                                        {
                                            "descripcion": "La Giralda de Sevilla es muy bonita porque yo he estado allí",
                                            "pais": "España"
                                        }
                                    ]
                                    """)


                    )
            )
    })
    public List<MonumentoResponse> getAll(){
        return servicio.getAll()
                .stream()
                .map(monumento -> MonumentoResponse.of(monumento))
                //.map(MonumentoResponse::of)
                .toList();
    }

    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Monumento encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MonumentoResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No se han encontrado monumentos",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                      "type": "http://dam.salesianos-triana.com/monumento-not-found",
                                                      "title": "Monumento no encontrado",
                                                      "status": 404,
                                                      "detail": "No se ha encontrado el monumento con id 1",
                                                      "instance": "/monumento/1"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<MonumentoResponse> getById(@PathVariable Long id){
        /*return ResponseEntity.of(
                servicio.getById(id)
                        .map(MonumentoResponse::of)
        );*/
        return ResponseEntity.ok(
                MonumentoResponse.of(servicio.getById(id)));
    }


    @PostMapping
    public ResponseEntity<MonumentoResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un nuevo monumento",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CrearMonumentoCmd.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "descripcion": "La Giralda de Sevilla es muy bonita porque yo he estado allí",
                                                "pais": "España"
                                            }
                                            """
                            )
                    )
            )
            @RequestBody CrearMonumentoCmd cmd
    ) {
        Monumento nuevo = servicio.save(cmd);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MonumentoResponse.of(nuevo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MonumentoResponse> edit(
            @PathVariable Long id,
            @RequestBody CrearMonumentoCmd cmd) {

        return ResponseEntity.ok(
                MonumentoResponse.of(
                    servicio.edit(cmd, id)
        ));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        servicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
