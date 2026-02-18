# Comandos para levantar cada entorno

## DEV
```shell
docker compose --env-file .env.dev -f docker-compose.dev.yml up -d --build
```
Logs:

```shell
docker compose --env-file .env.dev -f docker-compose.dev.yml logs -f api
```
Down:
```shell
docker compose --env-file .env.dev -f docker-compose.dev.yml down
```

### PROD
```shell
docker compose --env-file .env.prod -f docker-compose.prod.yml up -d --build
```

Down (mantiene datos):

```shell
docker compose --env-file .env.prod -f docker-compose.prod.yml down
```

Down borrando volumen (⚠️ elimina la BBDD):

```shell
docker compose --env-file .env.prod -f docker-compose.prod.yml down -v
```
