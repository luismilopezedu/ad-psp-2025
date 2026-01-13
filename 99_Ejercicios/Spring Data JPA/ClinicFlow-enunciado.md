# 🏥 EJERCICIO — ClinicFlow: Gestión de citas médicas

## 🎯 Objetivo del ejercicio
Desarrollar una **API REST con Spring Boot y Spring Data JPA** para gestionar las citas de un centro médico, aplicando correctamente:

- Modelado de entidades y asociaciones.
- Consultas derivadas y consultas JPQL.
- Uso preferente de **fetching LAZY**.
- Uso de **EntityGraph** para evitar problemas N+1.
- **Paginación**.
- Servicios con **lógica de negocio**.
- Controladores REST con **DTOs** para peticiones y respuestas.

---

## 📦 Modelo de dominio

### Entidades obligatorias
Debes implementar al menos las siguientes entidades:

- **Paciente**
  - id
  - nombre
  - email
  - fechaNacimiento

- **Profesional**
  - id
  - nombre
  - especialidad

- **Cita**
  - id
  - fechaHora
  - estado (PROGRAMADA, CANCELADA, ATENDIDA)

- **Consulta**
  - id
  - observaciones
  - diagnostico
  - fecha

### Relaciones
- Un **Paciente** puede tener muchas **Citas**.
- Un **Profesional** puede tener muchas **Citas**.
- Una **Cita** puede tener **una única Consulta**.
- Todas las asociaciones deben ser **LAZY**.

---

## ⚙️ Reglas de negocio (obligatorias)

Estas reglas deben implementarse en la **capa de servicio**, no en el controlador:

1. **Crear cita**
   - Un profesional **no puede tener dos citas a la misma fecha y hora**.
   - Un paciente **no puede tener más de una cita el mismo día**.
   - No se pueden crear citas en el pasado.

2. **Cancelar cita**
   - Solo se puede cancelar si la cita **no está ATENDIDA**.

3. **Registrar consulta**
   - Solo se puede registrar si la cita existe y está en estado PROGRAMADA.
   - Al registrar la consulta, la cita pasa automáticamente a ATENDIDA.

---

## 🔍 Consultas y repositorios

Debes implementar al menos:

- Consultas derivadas por nombre de método:
  - Citas por paciente.
  - Citas por estado.
  - Citas por rango de fechas.

- Al menos **una consulta JPQL**, por ejemplo:
  - Agenda diaria de un profesional incluyendo datos del paciente.

---

## 📄 DTOs obligatorios

No se pueden exponer entidades directamente desde los controladores.

DTOs mínimos requeridos:
- `CitaListDto`
- `CitaDetailDto`
- `CreateCitaRequest`
- `CreateConsultaRequest`

---

## 🌐 Endpoints REST mínimos

- `POST /citas`
- `PUT /citas/{id}/cancelar`
- `POST /citas/{id}/consulta`
- `GET /citas` (paginado y filtrable)
- `GET /pacientes/{id}/citas`

Al menos uno de los endpoints debe utilizar **paginación**.

---

## 🚀 Bonus (opcional)
- Uso explícito de **@EntityGraph** para evitar N+1.
- Filtro por especialidad del profesional.
- Manejo de errores con excepciones personalizadas.
