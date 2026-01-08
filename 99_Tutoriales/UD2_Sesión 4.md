## Sesión 4 — Fetching, Transacciones y Problemas Típicos en JPA

## 🎯 Objetivo de la sesión

Al finalizar esta sesión serás capaz de:

- Detectar problemas reales en aplicaciones JPA
- Interpretar los logs SQL generados por Hibernate
- Comprender por qué aparecen errores como:
  - `LazyInitializationException`
  - El problema **N+1**
- Proponer **soluciones razonadas**, no parches
- Empezar a pensar como un **desarrollador profesional**

---

## 1️⃣ Cuando JPA funciona… pero algo va mal

Hasta ahora has aprendido a:

- Crear entidades JPA
- Definir asociaciones (`@OneToMany`, `@ManyToOne`, etc.)
- Recuperar datos con repositorios

Muchas veces el código **funciona**, pero eso **no significa que esté bien hecho**.

En proyectos reales aparecen problemas como:

- Consultas innecesarias
- Lentitud en la aplicación
- Errores que solo aparecen al ejecutar
- Código difícil de mantener

👉 En esta sesión no vamos a añadir más anotaciones,  
👉 vamos a **entender qué está pasando realmente**.

---

## 2️⃣ Fetching en JPA: LAZY y EAGER (lo importante de verdad)

Cuando una entidad tiene relaciones con otras, JPA debe decidir **cuándo cargar esos datos**.

### 🔹 FetchType.EAGER

- La relación se carga automáticamente
- Parece cómodo
- **No es recomendable en proyectos reales**

¿Por qué?

- Carga datos que quizá no necesitas
- Genera consultas grandes
- Provoca problemas de rendimiento

---

### 🔹 FetchType.LAZY (opción recomendada)

- La relación **no se carga hasta que se usa**
- Más eficiente
- Obliga a diseñar mejor las consultas

⚠ Introduce problemas si no se entiende bien (los veremos ahora).

👉 **Regla profesional básica:**  
> Define las relaciones como `LAZY` y decide conscientemente cuándo cargar los datos.

---

## 3️⃣ Error típico: LazyInitializationException

### ❌ ¿Qué es?

Un error muy habitual en aplicaciones JPA:

```text
LazyInitializationException: could not initialize proxy – no Session
```

---

### ❓ ¿Por qué ocurre?

Sucede cuando:

1. Recuperas una entidad desde la base de datos
2. La transacción termina
3. Intentas acceder a una relación `LAZY`
4. Hibernate ya no tiene sesión abierta

Ejemplo típico:

```java
Curso curso = cursoRepository.findById(id).get();
// aquí termina la transacción

curso.getAlumnos().size(); // ❌ Error
```

---

### 🧠 Idea clave (muy importante)

> **El problema NO es usar LAZY**  
> El problema es **acceder a datos fuera de la transacción**

---

## 4️⃣ El gran enemigo del rendimiento: el problema N+1

### ❌ ¿Qué es el N+1?

Un problema muy común que afecta al rendimiento:

1. Se ejecuta **1 consulta** para obtener una lista
2. Por cada elemento de la lista, se ejecuta **otra consulta**
3. Resultado: **N + 1 consultas**

Ejemplo:

```java
List<Curso> cursos = cursoRepository.findAll();

for (Curso c : cursos) {
    System.out.println(c.getAlumnos().size());
}
```

---

### 📊 ¿Qué ocurre en la base de datos?

```sql
SELECT * FROM cursos;           -- 1 consulta
SELECT * FROM alumnos WHERE curso_id = 1;
SELECT * FROM alumnos WHERE curso_id = 2;
SELECT * FROM alumnos WHERE curso_id = 3;
...
```

👉 El código funciona,  
👉 pero **escala muy mal** cuando hay muchos datos.

---

## 5️⃣ Aprender a detectar problemas: leer los logs

Una habilidad clave como desarrollador es **leer los logs SQL**.

Configuración típica:

```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
```

Esto te permite:

- Ver cuántas consultas se lanzan
- Detectar N+1
- Relacionar tu código Java con el SQL real

🧠 **Mensaje profesional:**  
> Si no sabes qué consultas se están ejecutando,  
> no sabes realmente cómo funciona tu aplicación.

---

## 6️⃣ Estrategias para solucionar los problemas

No hay una única solución correcta.  
Hay **decisiones técnicas**.

---

### ✅ Opción 1: `JOIN FETCH` (JPQL)

Cargar relaciones en una sola consulta.

```java
@Query("""
   SELECT c FROM Curso c
   JOIN FETCH c.alumnos
""")
List<Curso> findAllWithAlumnos();
```

✔ Evita N+1  
⚠ Puede devolver duplicados  
⚠ No siempre funciona bien con paginación

---

### ✅ Opción 2: `@EntityGraph`

Forma más declarativa de indicar qué relaciones cargar.

```java
@EntityGraph(attributePaths = "alumnos")
List<Curso> findAll();
```

✔ Código más limpio  
✔ Muy usado en proyectos reales  
✔ Fácil de mantener

---

### ✅ Opción 3: Paginación

Cargar menos datos de cada vez.

```java
Page<Curso> findAll(Pageable pageable);
```

✔ Fundamental en aplicaciones reales  
✔ Mejora rendimiento  
⚠ No soluciona el N+1 por sí sola

---

### ✅ Opción 4: Revisar las transacciones

Asegurar que el acceso a datos ocurre dentro de una transacción.

```java
@Transactional
public CursoDTO obtenerCurso(Long id) {
    Curso curso = cursoRepository.findById(id).get();
    curso.getAlumnos().size();
    return mapper.toDto(curso);
}
```

✔ Evita `LazyInitializationException`  
⚠ No debe usarse para esconder un mal diseño

---

## 7️⃣ Pensar como un desarrollador profesional

En esta sesión debes aprender que:

- No todas las soluciones valen para todos los casos
- Cada decisión tiene consecuencias
- El contexto importa:
  - ¿Lista o detalle?
  - ¿Muchos datos?
  - ¿Necesito paginar?
  - ¿Estoy en un servicio o en un controlador?

> **JPA no es magia**  
> Es una herramienta potente que hay que usar con criterio.

---

🎯 **Objetivo final:**  
> Dejar de programar “porque funciona”  
> y empezar a programar **porque está bien diseñado**.

