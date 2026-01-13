# jpa-nplus1-lazy-bugs

Proyecto didáctico (Spring Boot + Spring Data JPA + Hibernate + H2) preparado **a propósito** con dos problemas clásicos:

1. **LazyInitializationException** en un endpoint de detalle.
2. **N+1 queries** en un endpoint de listado.

## Requisitos
- Java 17
- Maven 3.9+

## Ejecutar
```bash
mvn spring-boot:run
```

## Endpoints (con trampas)

### 1) N+1 (listado)
- `GET http://localhost:8080/courses`

Este endpoint construye un DTO con `studentsCount` usando `course.getStudents().size()`.
Como `students` es LAZY, Hibernate ejecuta:
- 1 query para cursos
- +1 query por cada curso para cargar estudiantes

**Observa el log SQL** (deberías ver muchas queries).

### 2) LazyInitializationException (detalle)
- `GET http://localhost:8080/courses/{id}`

Este endpoint obtiene un `Course` desde el service y, **en el controller**, intenta iterar `course.getStudents()`.
Como el service no está anotado con `@Transactional` y la relación es LAZY, se producirá:
`LazyInitializationException: could not initialize proxy - no Session`

## Datos de ejemplo
Al arrancar, se insertan:
- 20 cursos
- 30 alumnos por curso

## Objetivo en clase
- Detectar los problemas con logs.
- Proponer y aplicar soluciones:
  - `@EntityGraph`
  - `JOIN FETCH`
  - Paginación
  - Revisión de transacciones (dónde mapear a DTO, etc.)
