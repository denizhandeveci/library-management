# Local Development

to create or replace localdev db (containerized), run:

(Note: Examples use podman, for docker, simply replace `podman` with `docker`)

    podman run \
        --name deveci-mysql \
        --replace \
        -e MYSQL_ROOT_PASSWORD='4815162342bd.' \
        -e MYSQL_DATABASE=library_management \
        -p 3306:3306 \
        -d docker.io/library/mysql:8.0

to stop a running container:

`podman stop deveci-mysql`

to start an existing container:

`podman start deveci-mysql`