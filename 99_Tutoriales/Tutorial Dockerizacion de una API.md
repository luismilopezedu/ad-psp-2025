# 🐳 Tutorial de Dockerización de la API de Monumentos

------------------------------------------------------------------------

# 1️⃣ ¿Qué es un Dockerfile?

Un **Dockerfile** es un fichero de texto que contiene las instrucciones
necesarias para construir una **imagen Docker**.

## 📌 ¿Para qué sirve?

Permite:

-   Empaquetar una aplicación con todas sus dependencias.
-   Garantizar que se ejecuta igual en cualquier entorno.
-   Automatizar la construcción de imágenes.
-   Versionar la infraestructura como código.

En nuestro caso, lo usamos para:

> Construir la imagen Docker de la API Spring Boot y poder ejecutarla
> dentro de un contenedor.

------------------------------------------------------------------------

## 🧠 Sintaxis básica

Un Dockerfile se compone de instrucciones como:

``` dockerfile
FROM imagen_base
WORKDIR directorio
COPY origen destino
RUN comando
ENV variable=valor
EXPOSE puerto
ENTRYPOINT comando
```

Cada instrucción crea una **capa** en la imagen.

------------------------------------------------------------------------

# 2️⃣ ¿Qué es una imagen multistage?

Una **imagen multistage** utiliza múltiples instrucciones `FROM` dentro
del mismo Dockerfile.

## 📌 ¿Para qué sirve?

Permite:

-   Separar fase de construcción (build) y fase de ejecución (runtime).
-   Reducir el tamaño final de la imagen.
-   Eliminar herramientas innecesarias (como Maven).
-   Mejorar seguridad y rendimiento.

------------------------------------------------------------------------

### 💡 Ejemplo conceptual

``` dockerfile
FROM maven AS build
# compila la aplicación

FROM eclipse-temurin AS runtime
# solo ejecuta el jar
```

El resultado final solo contiene lo necesario para ejecutar la
aplicación.

------------------------------------------------------------------------

# 3️⃣ Dockerfile del proyecto

Nuestro Dockerfile tiene dos fases:

------------------------------------------------------------------------

## 🔹 Stage 1 --- Build

``` dockerfile
FROM maven:3-eclipse-temurin-25 AS build
WORKDIR /app

COPY pom.xml ./
COPY .mvn/ .mvn/
COPY mvnw ./

RUN chmod +x mvnw && sed -i 's/\r$//' mvnw
RUN ./mvnw -DskipTests dependency:go-offline

COPY src/ src/
RUN ./mvnw -DskipTests package
```

### ¿Qué hace?

1.  Usa una imagen con Maven + JDK 25.
2.  Descarga dependencias (optimiza cache).
3.  Compila el proyecto.
4.  Genera el jar en `/target`.

------------------------------------------------------------------------

## 🔹 Stage 2 --- Runtime

``` dockerfile
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=build /app/target/*.jar /app/app.jar
COPY docker/entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

USER spring:spring

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseContainerSupport"
EXPOSE 8080

ENTRYPOINT ["/app/entrypoint.sh"]
```

### ¿Qué hace?

-   Usa solo JRE (no JDK).
-   Ejecuta como usuario no root.
-   Copia únicamente el jar.
-   Usa un entrypoint personalizado.
-   Expone el puerto 8080.

👉 Resultado: imagen ligera, segura y profesional.

------------------------------------------------------------------------

# 4️⃣ docker-compose para cada perfil

Tenemos dos ficheros separados:

-   `docker-compose.dev.yml`
-   `docker-compose.prod.yml`

------------------------------------------------------------------------

## 🔹 Perfil DEV

Incluye:

-   API
-   PostgreSQL
-   pgAdmin
-   Base de datos en memoria (tmpfs)

Características:

-   Puerto DB expuesto.
-   Volumen en memoria.
-   Hibernate `create`.
-   Mayor verbosidad.

------------------------------------------------------------------------

## 🔹 Perfil PROD

Incluye:

-   API
-   PostgreSQL
-   Volumen persistente

Características:

-   Sin pgAdmin.
-   Volumen en disco.
-   Hibernate `validate` (en nuestro caso debería estar en `create` porque realmente no tenemos nada en la base de datos).
-   Configuración más segura.

------------------------------------------------------------------------

## 📌 Nota importante

Como ahora usamos Docker Compose directamente:

``` properties
spring.docker.compose.enabled=false
```

Ya no usamos **Spring Boot Docker Compose Support**.

La configuración de conexión se hace vía variables de entorno:

``` yaml
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD_FILE
```

------------------------------------------------------------------------

# 5️⃣ ¿Qué son los secretos?

Un **secret** permite almacenar información sensible fuera del código:

-   Contraseñas
-   Tokens
-   Claves API

En nuestro proyecto:

    secrets/
     ├─ dev_db_password.txt
     └─ prod_db_password.txt

En docker-compose:

``` yaml
secrets:
  db_password:
    file: ./secrets/dev_db_password.txt
```

Y se monta automáticamente en:

    /run/secrets/db_password

------------------------------------------------------------------------

## 🔐 Ventajas

-   No aparecen en el YAML.
-   No aparecen en docker inspect.
-   No se almacenan en la imagen.
-   Separación clara configuración / secretos.

------------------------------------------------------------------------

# 6️⃣ ¿Por qué necesitamos `entrypoint.sh`?

Spring Boot **no soporta directamente**:

    SPRING_DATASOURCE_PASSWORD_FILE

Por eso necesitamos un script que:

1.  Lea el fichero secret.
2.  Extraiga la contraseña.
3.  La exporte como variable real.
4.  Arranque la aplicación.

------------------------------------------------------------------------

## 📄 `entrypoint.sh`

``` sh
#!/bin/sh
set -eu

if [ "${SPRING_DATASOURCE_PASSWORD:-}" = "" ] && [ -n "${SPRING_DATASOURCE_PASSWORD_FILE:-}" ]; then
  export SPRING_DATASOURCE_PASSWORD="$(cat "${SPRING_DATASOURCE_PASSWORD_FILE}")"
fi

exec java ${JAVA_OPTS:-} -jar /app/app.jar
```

Este script se ejecuta al arrancar el contenedor.

------------------------------------------------------------------------

# 7️⃣ Comandos para construir y ejecutar

------------------------------------------------------------------------

## 🚀 DEV

Construir y levantar:

``` bash
docker compose --env-file .env.dev -f docker-compose.dev.yml up -d --build
```

Ver logs:

``` bash
docker compose --env-file .env.dev -f docker-compose.dev.yml logs -f api
```

Parar:

``` bash
docker compose --env-file .env.dev -f docker-compose.dev.yml down
```

------------------------------------------------------------------------

## 🚀 PROD

Construir y levantar:

``` bash
docker compose --env-file .env.prod -f docker-compose.prod.yml up -d --build
```

Parar manteniendo datos:

``` bash
docker compose --env-file .env.prod -f docker-compose.prod.yml down
```

Parar borrando datos:

``` bash
docker compose --env-file .env.prod -f docker-compose.prod.yml down -v
```

------------------------------------------------------------------------

# 🎯 Conclusión

Con esta dockerización hemos conseguido:

-   Imagen multistage optimizada
-   Separación build/runtime
-   Seguridad con secrets
-   Configuración externa
-   Dos entornos reales (dev/prod)
-   Persistencia en producción
-   Buenas prácticas profesionales
