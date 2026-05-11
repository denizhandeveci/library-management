# Local Development

## Database

Examples use Podman. For Docker, replace `podman` with `docker`.

### Create a fresh local database container

```bash
podman run \
    --replace \
    --name deveci-mysql \
    -e MYSQL_ROOT_PASSWORD='4815162342bd.' \
    -e MYSQL_DATABASE=library_management \
    -p 3306:3306 \
    -d docker.io/library/mysql:8.0
````

Wait until MySQL is ready:

```bash
podman logs -f deveci-mysql
```

Look for:

```text
ready for connections
```

### Start an existing container

```bash
podman start deveci-mysql
```

### Stop the container

```bash
podman stop deveci-mysql
```

### Run backend migrations

Flyway runs automatically when the Spring Boot backend starts:

```bash
./mvnw spring-boot:run
```

### Seed local dummy data

After the backend has started once and Flyway has created the schema:

```bash
podman exec -i deveci-mysql mysql -uroot -p4815162342bd. library_management < src/localdev/seed-data.sql
```

The seed script is only for local development. It is not part of the Flyway migrations.
