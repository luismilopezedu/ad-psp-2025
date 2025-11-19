# Tutorial: Documentación de Endpoints REST con OpenAPI 3 y Springdoc

## 1. ¿Qué es OpenAPI 3?
OpenAPI 3 es un estándar para describir APIs REST de forma legible por humanos y máquinas. Permite documentar endpoints, parámetros, respuestas y modelos, generar clientes automáticamente y obtener Swagger UI.

---

## 2. Añadir Springdoc a Spring Boot

### Dependencia Maven

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>X.XX.XXX</version> <!-- Última versión disponible>
</dependency>
```

La última versión disponible se puede consultar en [https://springdoc.org/#getting-started](https://springdoc.org/#getting-started)

Accesos:
- Swagger UI → http://localhost:8080/swagger-ui.html
- JSON → http://localhost:8080/v3/api-docs

---

## 3. Anotaciones esenciales

- `@Tag` (a nivel de controlador)
- `@Operation`
- `@ApiResponses` / `@ApiResponse`
- `@Parameter`
- `@RequestBody`
- `@Schema`, `@ArraySchema`
- `@ExampleObject`

---

## 4. Documentación de parámetros

```java
@GetMapping
@Operation(summary = "Obtiene todos los productos filtrados o no")
public List<ProductDto> getAll(
        @Parameter(description = "Precio máximo", example = "1500")
        @RequestParam(required = false, defaultValue = "-1") double maxPrice,
        @Parameter(description = "Orden: asc o desc", example = "asc")
        @RequestParam(required = false, defaultValue = "no") String sort
) {
    return productService.query(maxPrice, sort);
}
```

---

## 5. Documentación de RequestBody

```java
@PostMapping
@Operation(summary = "Crea un nuevo producto")
@ApiResponse(responseCode = "201", description = "Producto creado correctamente")
public ProductDto create(
        @RequestBody(
                description = "Datos del nuevo producto",
                required = true,
                content = @Content(
                        schema = @Schema(implementation = CreateProductDto.class),
                        examples = @ExampleObject(
                                value = "{ "name": "Laptop RTX", "price": 1899.99 }"
                        )
                )
        )
        @Valid CreateProductDto dto
) {
    return productService.create(dto);
}
```

---

## 6. Documentación de respuestas

```java
@ApiResponse(responseCode = "404", description = "Producto no encontrado")
```

```java
@ApiResponse(
    responseCode = "200",
    description = "Producto encontrado",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ProductDto.class)
    )
)
```

---

## 7. Documentar DTOs

```java
public record ProductDto(
        @Schema(description = "ID del producto", example = "1") Long id,
        @Schema(description = "Nombre", example = "Smartphone X12") String name,
        @Schema(description = "Precio", example = "999.99") double price
) {}
```

---

## 8. Ejemplo completo

```java
@GetMapping
@Operation(
        summary = "Obtiene todos los productos",
        description = "Devuelve una lista de productos. Puede filtrarse por precio y ordenarse."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Se han encontrado productos",
                content = @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = GetProductListDto.class)),
                        examples = @ExampleObject(
                                value = "[{ "id":1, "name":"Laptop", "price":1234.56 }]"
                        )
                )
        ),
        @ApiResponse(responseCode = "404", description = "No se ha encontrado ningún producto")
})
public GetProductListDto getAll(
        @Parameter(description = "Precio máximo", example = "1500")
        @RequestParam(required = false, defaultValue = "-1") double maxPrice,
        @Parameter(description = "Orden", example = "asc")
        @RequestParam(required = false, defaultValue = "no") String sortDirection
) {
    return GetProductListDto.of(productService.query(maxPrice, sortDirection));
}
```

---

## 9. Buenas prácticas

- Usa *summary* claro.
- Incluye ejemplos útiles.
- Describe todos los parámetros.
- Documenta todos los códigos de estado.
- Usa `@Schema` en DTOs.
- Utiliza `@ArraySchema` para listas.

---

## 10. Configuración opcional

```yaml
springdoc:
  api-docs:
    enabled: true
  swagger-ui:
    path: /swagger
    operationsSorter: alpha
```
