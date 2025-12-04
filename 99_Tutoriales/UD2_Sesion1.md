# Minitutorial — Sesión 1: ORM, JPA y Diseño de Entidades en Spring Data JPA

## 1. Introducción
En esta primera sesión vamos a sentar las bases del trabajo con persistencia en Spring Boot. Comprenderemos por qué existe el concepto de ORM, qué aporta realmente JPA dentro del ecosistema Java y cómo encajan Hibernate y Spring Data JPA. Finalmente, pasaremos a algo esencial en cualquier proyecto: modelar entidades correctamente.

## 2. ¿Qué problema resuelve un ORM?
El ORM traduce el mundo orientado a objetos y el mundo relacional.

### Diferencias típicas:
| Java | SQL |
|------|------|
| Herencia | No existe |
| Tipos ricos | Tipos simples |
| Relaciones naturales | Claves externas |
| Identidad por referencia | Identidad por valores |

## 3. JPA, Hibernate y Spring Data JPA
**JPA:** especificación.  
**Hibernate:** implementación de JPA.  
**Spring Data JPA:** capa que simplifica el acceso a datos.

## 4. Entidades
Una entidad representa un concepto de dominio y se persiste como una fila.

### Ejemplo:
```java
@Entity
public class Pokemon {
    @Id
    @GeneratedValue
    private Long id;
    private String nombre;
    private int nivel;
    private String tipo;
}
```

## 5. Buenas prácticas
- Prioriza el dominio.  
- Usa tipos adecuados (LocalDate, BigDecimal, Enum).  
- No dupliques información.  
- Define restricciones con @Column.

## 6. Actividad: Mini-entidad Pokémon
Los alumnos diseñan una entidad con atributos, tipos, anotaciones y diagrama simple.

## 7. Mini-reto final
Añadir:
- Un Enum  
- Un LocalDate  
- Una restricción @Column  

## 8. Reflexión final
> ¿En qué se diferencia modelar una entidad pensando en el dominio frente a pensar en tablas?

# Bibliografía

- Curso [Fundamentos de Spring Data JPA en Openwebinars.net](https://academia.openwebinars.net/portada/introduccion-spring-data-jpa/)
- Documentación de [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/jpa.html)


  