# Local Development

## Environment configuration

The backend reads local configuration from environment variables.

Create a local `.env` file from the template in the project root:

```bash
cp .env.template .env
```

Then edit `.env` if needed.

For local JWT secrets, you can generate a value with:

```bash
openssl rand -base64 48
```

The `.env` file is ignored by Git and must not be committed.

## Database

Examples use Podman. For Docker, replace `podman` with `docker`.

### Create a fresh local database container

From the project root:

```bash
source .env

podman run \
    --replace \
    --name deveci-mysql \
    -e MYSQL_ROOT_PASSWORD="$DB_PASSWORD" \
    -e MYSQL_DATABASE=library_management \
    -p 3306:3306 \
    -d docker.io/library/mysql:8.0
```

### Start an existing container

```bash
podman start deveci-mysql
```

### Stop the container

```bash
podman stop deveci-mysql
```

## Backend

### Run the backend locally

Recommended command from the project root:

```bash
./run-local.sh
```

This script loads `.env` and runs:

```bash
./mvnw spring-boot:run
```

Flyway runs automatically when the backend starts.

If you start the backend from IntelliJ, add the variables from `.env` to your Spring Boot run configuration, or use an EnvFile plugin.

### Seed local dummy data

After the backend has started once and Flyway has created the schema:

```bash
source .env

podman exec -i deveci-mysql mysql -u"$DB_USERNAME" -p"$DB_PASSWORD" library_management < src/localdev/seed-data.sql
```

The seed script is only for local development. It is not part of the Flyway migrations.