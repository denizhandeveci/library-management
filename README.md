## Backend

### 1. Start the local database

Refer to [localdev guide](src/localdev/localdev.md) for details.

Credentials can also be found [here](/src/main/resources/application.properties).

### 2. Run the backend

```bash
./mvnw spring-boot:run
````

### 3. Optional: seed local dummy data

After the backend has started once and Flyway has created the schema, run:

```bash
podman exec -i deveci-mysql mysql -uroot -p4815162342bd. library_management < src/localdev/seed-data.sql
```
