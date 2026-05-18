## Backend

### 1. Create local environment file

Create your local `.env` file from the template:

```bash
cp .env.template .env
```

Update the values in `.env` if needed.

The `.env` file is ignored by Git and must not be committed.

### 2. Start the local database

Refer to the [localdev guide](src/localdev/localdev.md) for database setup and local development commands.

### 3. Run the backend

Recommended local start command:

```bash
./run-local.sh
```

This loads `.env` and starts the backend via Maven.

If you start the backend from IntelliJ, add the variables from `.env` to your Spring Boot run configuration, or use an EnvFile plugin.

### 4. Optional: seed local dummy data

See the [localdev guide](src/localdev/localdev.md#seed-local-dummy-data).