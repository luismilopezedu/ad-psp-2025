# Seguridad a nivel de método en Spring Security

Spring Security permite aplicar reglas de autorización directamente sobre los métodos del código fuente.  
Existen **tres grandes grupos de anotaciones**, cada uno con su propósito y nivel de expresividad.

---

## `@PreAuthorize` y `@PostAuthorize` (junto con `@PreFilter` / `@PostFilter`)

**Pertenece a:** Spring Security (Expression-Based Method Security, SpEL)  
**Se activan con:** `@EnableMethodSecurity(prePostEnabled = true)`

### `@PreAuthorize`
- Evalúa **antes de ejecutar el método**.  
- Usa **expresiones SpEL** para comprobar condiciones complejas.  
- Puede acceder a roles, permisos, parámetros o propiedades del usuario autenticado.  
- Si la expresión no se cumple → el método **no se ejecuta**.

```java
@PreAuthorize("hasAuthority('ACCOUNT_READ') or #id == authentication.principal.id")
public Account getAccount(Long id) { ... }
```

Solo se ejecuta si el usuario tiene `ACCOUNT_READ` o es el propietario del recurso.

---

### `@PostAuthorize`
- Evalúa **después de ejecutar el método**.  
- Útil cuando necesitas comprobar el **resultado devuelto** (por ejemplo, propiedad del objeto).

```java
@PostAuthorize("returnObject.owner == authentication.name")
public Account getAccount(Long id) { ... }
```

El método se ejecuta, pero se lanza un `AccessDeniedException` si la condición no se cumple.

---

### `@PreFilter` y `@PostFilter`
- Permiten **filtrar colecciones de entrada o salida** basadas en expresiones de seguridad.  
- Muy útiles para APIs que manejan listas.

```java
@PostFilter("filterObject.owner == authentication.name")
public List<Account> getAllAccounts() { ... }
```

Devuelve solo los objetos cuyo propietario coincide con el usuario autenticado.

---

### **Ventajas**
- Expresivas y potentes gracias a SpEL.  
- Permiten control **contextual y granular**.  
- Perfectas para casos donde la seguridad depende de datos o contexto.

### **Inconvenientes**
- Expresiones complejas pueden ser difíciles de mantener.  
- Sobrecarga innecesaria para reglas simples.

---

# Spring Expression Language (SpEL) aplicado a Spring Security

## 1. ¿Qué es SpEL?

SpEL (Spring Expression Language) es un lenguaje de expresiones integrado en el ecosistema Spring.

Permite:
- Acceder a propiedades, métodos y variables en tiempo de ejecución.  
- Realizar evaluaciones lógicas y comparaciones.  
- Usar en configuraciones, plantillas, anotaciones y, especialmente, en seguridad.

Ejemplo básico fuera del contexto de seguridad:

```java
ExpressionParser parser = new SpelExpressionParser();
Expression exp = parser.parseExpression("'Hola'.concat(' mundo!')");
String result = exp.getValue(String.class); // "Hola mundo!"
```

---

## 2. SpEL en Spring Security

Spring Security usa SpEL para evaluar condiciones de acceso en anotaciones de método.

Se activa al usar:
```java
@EnableMethodSecurity(prePostEnabled = true)
```

Y se aplica en:
- `@PreAuthorize` → antes de ejecutar el método.  
- `@PostAuthorize` → después del método.  
- `@PreFilter` / `@PostFilter` → para filtrar colecciones.

---

## 3. Objetos disponibles en el contexto de SpEL

Spring Security expone automáticamente varios objetos al evaluar expresiones SpEL:

| Objeto | Tipo | Descripción |
|---------|------|-------------|
| authentication | Authentication | Objeto completo de autenticación del usuario actual. |
| principal | UserDetails | Detalles del usuario autenticado. |
| #root / #this | Object | Contexto raíz de evaluación. |
| #target | Object | Objeto destino (el bean donde se ejecuta el método). |
| #args | Object[] | Lista de argumentos del método. |
| #<paramName> | Variable | Permite acceder a los parámetros del método por nombre. |

Ejemplo:
```java
@PreAuthorize("#id == authentication.principal.id")
public User getUserById(Long id) { ... }
```

Permite que cada usuario solo acceda a su propio perfil.

---

## 4. Operadores y funciones comunes en SpEL

| Tipo | Ejemplo | Descripción |
|------|----------|-------------|
| Comparación | ==, !=, <, >, <=, >= | Comparaciones directas |
| Lógicos | and, or, ! | Combinaciones de condiciones |
| Texto | 'ROLE_ADMIN' eq 'ROLE_ADMIN' | Comparación de cadenas |
| Colecciones | #roles.contains('ADMIN') | Comprueba pertenencia |
| Ternario | cond ? expr1 : expr2 | Evaluación condicional |
| Invocación | #param.startsWith('A') | Invoca métodos Java |

---

## 5. Ejemplos prácticos de uso con anotaciones

### Control por roles o permisos
```java
@PreAuthorize("hasRole('ADMIN') or hasAuthority('USER_READ')")
public List<User> listUsers() { ... }
```

### Acceso basado en parámetros
```java
@PreAuthorize("#userId == authentication.principal.id")
public User getUser(Long userId) { ... }
```

### Validación sobre el resultado
```java
@PostAuthorize("returnObject.owner == authentication.name")
public Account getAccount(Long id) { ... }
```

### Filtrado de listas devueltas
```java
@PostFilter("filterObject.owner == authentication.name")
public List<Account> getAllAccounts() { ... }
```

---

## 6. Personalización avanzada: servicios dentro de SpEL

También puedes invocar beans de Spring desde las expresiones:

```java
@PreAuthorize("@permissionEvaluator.canEdit(authentication, #postId)")
public void editPost(Long postId) { ... }
```

- El bean `permissionEvaluator` debe estar en el contexto de Spring.  
- Esto permite centralizar lógica de permisos más compleja.

---

## 7. Consejos y buenas prácticas

- Mantén las expresiones claras y legibles.  
  Ejemplo: evita `@PreAuthorize("hasRole('ADMIN') and !#user.active or #id == principal.id")`.  
  En su lugar, usa un método auxiliar.

- Centraliza la lógica compleja en servicios reutilizables (`@Component` + métodos llamados desde SpEL).

- No abuses de SpEL en lógica de negocio; úsalo solo para autorización.

- Documenta las expresiones y los roles implicados para que otros desarrolladores comprendan las reglas.

---

## 8. Ejemplo completo

```java
@Service
public class DocumentService {

    // Solo el autor o un administrador pueden editar
    @PreAuthorize("#doc.author == authentication.name or hasRole('ADMIN')")
    public void edit(Document doc) { ... }

    // Solo el autor puede ver su documento
    @PostAuthorize("returnObject.author == authentication.name")
    public Document view(Long id) { ... }

    // Filtra resultados
    @PostFilter("filterObject.visibility == 'PUBLIC' or filterObject.author == authentication.name")
    public List<Document> listAll() { ... }
}
```

---

## 9. Conclusión

- SpEL convierte la seguridad de Spring en un sistema declarativo, expresivo y contextual.  
- Permite integrar la autenticación, los parámetros del método y la lógica de negocio  
para tomar decisiones precisas de autorización.

---

## Referencias

- [Spring Security Reference – Method Security](https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html)
- [Baeldung – Spring Expression Language Guide](https://www.baeldung.com/spring-expression-language)
- [Spring Docs – SpEL Syntax Overview](https://docs.spring.io/spring-framework/reference/core/expressions.html)
