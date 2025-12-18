
# 📘 GUÍA PARA EL ALUMNADO  
## Servicios y lógica de negocio en Spring Data JPA

---

## 🎯 Objetivo de esta guía

Al finalizar esta guía serás capaz de:

- Entender qué es la **lógica de negocio** de una aplicación.
- Identificar **servicios anémicos**.
- Diseñar **servicios con reglas reales**, más allá del CRUD.
- Aplicar reglas de negocio usando **servicios, entidades y excepciones** en Spring Boot.

---

## 1️⃣ ¿Qué es la lógica de negocio?

La **lógica de negocio** son las **reglas que definen cómo funciona una aplicación**, independientemente de:

- La base de datos  
- El framework utilizado  
- La interfaz (web, móvil, API REST, etc.)

### Ejemplos

- “No se puede crear una tarea sin título”
- “Un usuario no puede tener más de 5 tareas activas”
- “Solo el propietario puede modificar una tarea”
- “No se puede finalizar dos veces la misma tarea”

👉 Si quitamos Spring o la base de datos, estas reglas **seguirían siendo válidas**.

---

## 2️⃣ El papel del servicio

En Spring, un **servicio** representa un **caso de uso**:

- Crear una tarea  
- Completar una tarea  
- Cambiar el título de una tarea  

Un servicio **no es** solo un intermediario entre el controlador y el repositorio.

---

## 3️⃣ Servicios anémicos (qué son y por qué evitarlos)

### ❌ Ejemplo de servicio anémico

```java
@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Task create(Task task) {
        return repository.save(task);
    }

    public Task findById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
```

### Problemas de este diseño

- No hay reglas de negocio
- No hay decisiones
- No hay control del dominio
- El servicio no aporta valor real

---

## 4️⃣ Servicios con lógica de negocio

Un **servicio con lógica de negocio**:

- Comprueba reglas del dominio
- Lanza excepciones si las reglas no se cumplen
- Coordina entidades y repositorios
- Representa claramente un **caso de uso**

---

## 5️⃣ Ejercicio 1 – Detectar un servicio anémico

### Enunciado

Observa el siguiente servicio:

```java
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task create(Task task) {
        return taskRepository.save(task);
    }

    public Task markDone(Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        task.setStatus(TaskStatus.DONE);
        return taskRepository.save(task);
    }
}
```

### Preguntas

1. ¿Qué reglas de negocio se están aplicando?
2. ¿Qué errores podrían producirse?
3. ¿Qué decisiones debería tomar el servicio y no está tomando?

✍️ **Indica al menos 3 problemas de este diseño.**

---

## 6️⃣ Ejercicio 2 – Añadir reglas de negocio

### Reglas a implementar

1. No se puede crear una tarea sin título.
2. No se puede marcar como DONE una tarea ya finalizada.
3. Si la tarea no existe, se debe lanzar una excepción.

### Paso 1 – Excepción de negocio

```java
public class BusinessRuleViolation extends RuntimeException {
    public BusinessRuleViolation(String message) {
        super(message);
    }
}
```

### Paso 2 – Refactoriza el servicio

✍️ Modifica el servicio para que cumpla las reglas anteriores.

**Pistas:**
- Usa `orElseThrow`
- No confíes solo en setters
- El servicio debe decidir si la operación es válida

---

## 7️⃣ Ejercicio 3 – Autorización de negocio

### Contexto

Cada tarea tiene un `userId`.  
Un usuario solo puede modificar **sus propias tareas**.

### Enunciado

Implementa el método:

```java
public Task changeTitle(Long userId, Long taskId, String newTitle)
```

Debe cumplirse:

- La tarea debe existir
- El `userId` debe coincidir con el propietario
- El nuevo título no puede estar vacío

Si alguna regla falla, lanza una excepción adecuada.

---

## 8️⃣ Ejercicio 4 – Regla global del sistema

### Contexto

Un usuario **no puede tener más de 5 tareas activas** (`OPEN` o `IN_PROGRESS`).

### Enunciado

1. Añade al repositorio un método para contar tareas activas por usuario.
2. Implementa la regla en el servicio al crear una tarea.
3. Lanza una excepción si se supera el límite.

💡 Esta regla pertenece al **servicio**, no a la entidad.

---

## 9️⃣ Ejercicio 5 – Pensamiento de dominio

Completa la tabla:

| Regla | ¿Entidad? | ¿Servicio? | Justificación |
|----|----|----|----|
| El título no puede estar vacío | | | |
| No marcar DONE dos veces | | | |
| Máximo 5 tareas activas | | | |
| Solo el dueño puede modificar | | | |

---

## 🔑 Ideas clave para recordar

- Un **servicio** representa un **caso de uso**
- La lógica de negocio:
  - No es CRUD
  - No es SQL
  - No es solo validación técnica
- Un buen servicio:
  - Decide
  - Protege reglas
  - Explica cómo funciona el sistema

---

## 🚀 Para subir nota (opcional)

- Elimina setters públicos innecesarios
- Mueve reglas simples a métodos de la entidad
- Añade tests de servicio para validar reglas
