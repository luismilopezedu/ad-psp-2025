# 🚚 EJERCICIO — FleetManager: Gestión de flotas y mantenimientos

## 🎯 Objetivo del ejercicio
Implementar una **API REST con Spring Boot y Spring Data JPA** para gestionar una flota de vehículos, conductores y revisiones de mantenimiento, aplicando buenas prácticas de diseño y acceso a datos.

---

## 📦 Modelo de dominio

### Entidades obligatorias
- **Vehiculo**
  - id
  - matricula
  - modelo
  - kmActuales
  - estado (DISPONIBLE, ASIGNADO, EN_MANTENIMIENTO)

- **Conductor**
  - id
  - nombre
  - email

- **Asignacion**
  - id
  - fechaInicio
  - fechaFin

- **Mantenimiento**
  - id
  - tipo
  - fecha
  - kmEnRevision

- **Taller**
  - id
  - nombre
  - ciudad

### Relaciones
- Un **Vehiculo** puede tener muchas **Asignaciones**.
- Un **Conductor** puede tener muchas **Asignaciones**.
- Un **Vehiculo** puede tener muchos **Mantenimientos**.
- Un **Taller** puede realizar muchos **Mantenimientos**.
- Todas las asociaciones deben ser **LAZY**.

---

## ⚙️ Reglas de negocio (obligatorias)

1. **Asignar vehículo**
   - Un vehículo **solo puede tener una asignación activa**.
   - Un conductor **solo puede tener una asignación activa**.

2. **Registrar mantenimiento**
   - No se puede registrar un mantenimiento si el vehículo está asignado.
   - El kilometraje del mantenimiento debe ser **mayor o igual** al km actual del vehículo.
   - Al registrar el mantenimiento, se actualiza el kilometraje del vehículo.

3. **Cerrar asignación**
   - Marca la fecha de fin.
   - El vehículo vuelve a estado DISPONIBLE.

4. La matrícula del vehículo debe ser **única**.

---

## 🔍 Consultas y repositorios

Debes implementar:
- Consultas derivadas:
  - Vehículos por estado.
  - Mantenimientos de un vehículo.

- Al menos una consulta JPQL:
  - Último mantenimiento de un vehículo.
  - Vehículos con su último mantenimiento.

---

## 📄 DTOs obligatorios

- `VehiculoSummaryDto`
- `VehiculoDetailDto`
- `CreateAsignacionRequest`
- `CreateMantenimientoRequest`

---

## 🌐 Endpoints REST mínimos

- `POST /asignaciones`
- `PUT /asignaciones/{id}/cerrar`
- `POST /mantenimientos`
- `GET /vehiculos` (paginado)
- `GET /conductores/{id}/vehiculo-activo`

---

## 🚀 Bonus (opcional)
- Uso de **EntityGraph** para evitar N+1.
- Búsqueda por matrícula o modelo.
- Validaciones avanzadas y excepciones personalizadas.
