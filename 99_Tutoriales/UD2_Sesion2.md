# TUTORIAL – Sesión 2: Asociaciones con JPA y Spring Data JPA  
## *1:N, N:1, N:M, direccionalidad, fetch, cascada y buenas prácticas modernas*

## 1. Introducción
En esta sesión profundizamos en uno de los aspectos más delicados y decisivos del modelado de datos en aplicaciones modernas: **las asociaciones entre entidades en JPA**.

Aunque el concepto parece sencillo —"una entidad se relaciona con otra"—, la implementación en JPA implica decisiones que afectan a:

- rendimiento,
- mantenimiento del código,
- consultas generadas por Hibernate,
- y la estabilidad de la API REST.

Por ello, vamos a estudiar no solo cómo se representan las asociaciones, sino también **cómo usarlas bien**, apoyándonos en las recomendaciones del equipo de Hibernate y de la comunidad Spring Data.

---

## 2. Direccionalidad: ¿unidireccional o bidireccional?

### 2.1. ¿Qué significa direccionalidad?
- **Unidireccional:** una entidad conoce a la otra, pero no al revés.  
- **Bidireccional:** ambas entidades mantienen una referencia mutua.

Ejemplo conceptual:

```
Personaje → Arma        (unidireccional)
Personaje ↔ Arma        (bidireccional)
```

### 2.2. Qué recomienda Hibernate… y por qué
La documentación oficial de Hibernate recomienda **preferir asociaciones bidireccionales**, siempre que representen una relación real en el dominio.

#### Motivos:
1. Coherencia de navegación  
2. Facilidad para queries complejas  
3. Mejor reflejo del dominio  
4. Evita sorpresas (especialmente en 1:N unidireccional)

---

## 3. Asociación @ManyToOne (N:1)

Ejemplo: muchos *Pokémon* pertenecen a una *Región*.

```java
@Entity
public class Pokemon {

    @Id @GeneratedValue
    private Long id;

    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    private Region region;
}
```

**Puntos clave:**
- LAZY recomendado  
- Ideal para APIs REST  
- Evita explosión de datos y ciclos infinitos  

---

## 4. Asociación @OneToMany (1:N)

```java
@Entity
public class Region {

    @Id @GeneratedValue
    private Long id;

    private String nombre;

    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY)
    private List<Pokemon> pokemons = new ArrayList<>();
}
```

**Importante:**  
- El lado propietario es siempre ManyToOne  
- mappedBy evita tablas intermedias no deseadas  

---

## 5. Asociación @ManyToMany (N:M)

```java
@Entity
public class Entrenador {

    @Id @GeneratedValue
    private Long id;
    private String nombre;

    @ManyToMany
    @JoinTable(
        name = "entrenador_mision",
        joinColumns = @JoinColumn(name = "entrenador_id"),
        inverseJoinColumns = @JoinColumn(name = "mision_id")
    )
    private Set<Mision> misiones = new HashSet<>();
}
```

Si necesitas información adicional → usa **entidad intermedia**.

---

## 6. Asociación con atributos extra (entidad intermedia)

```java
@Entity
public class MagoHechizo {

    @EmbeddedId
    private MagoHechizoId id;

    @ManyToOne
    @MapsId("magoId")
    private Mago mago;

    @ManyToOne
    @MapsId("hechizoId")
    private Hechizo hechizo;

    private int maestria;
}
```

### Clase de clave primaria (MagoHechizoId)

```java
@Embeddable
public class MagoHechizoId implements Serializable {

    private Long magoId;
    private Long hechizoId;

    public MagoHechizoId() {}

    public MagoHechizoId(Long magoId, Long hechizoId) {
        this.magoId = magoId;
        this.hechizoId = hechizoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MagoHechizoId that)) return false;
        return Objects.equals(magoId, that.magoId) &&
               Objects.equals(hechizoId, that.hechizoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(magoId, hechizoId);
    }
}
```

---

## 7. EAGER vs LAZY: por qué evitar EAGER

EAGER puede producir:
- Cargas excesivas  
- N+1 queries  
- Problemas en APIs REST  
- Rendimiento imprevisible  

**Recomendación:** usar **LAZY** casi siempre.

---

## 8. Cascada: cómo manejar el borrado en cascada

```java
@OneToMany(mappedBy = "region", cascade = CascadeType.REMOVE, orphanRemoval = true)
private List<Pokemon> pokemons = new ArrayList<>();
```

- REMOVE → borra hijos al borrar el padre  
- orphanRemoval → borra hijos al sacarlos de la colección  

---

## 9. Resumen visual

| Asociación | Lado propietario | Uso típico | Notas |
|-----------|------------------|------------|-------|
| ManyToOne | ManyToOne        | N → 1      | LAZY recomendado |
| OneToMany | ManyToOne        | 1 → N      | mappedBy necesario |
| ManyToMany | Ambos | N ↔ N | Evitar si hay datos extra |
| Intermedia | Intermedia | Relaciones ricas | Más flexible |

---

## 10. Preguntas de reflexión

1. ¿Cuándo es peligroso aplicar cascada?  
2. ¿Por qué ManyToMany puede ser problemático?  
3. ¿Cómo afecta la direccionalidad a APIs REST?  
4. ¿Cuándo introducir una entidad intermedia?  
5. ¿Qué ocurre si usas EAGER en todo el modelo?

---

## 11. Práctica final

Modelar:

- Profesor  
- Curso  
- Alumno  

Con:
- Profesor 1:N Curso  
- Curso N:M Alumno  
- Entidad intermedia con nota final  

---

