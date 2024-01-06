# Fantasy map server

### Setting environmental variables
.env
```
SERVER_PORT=
DB_SERVER=
DB_DATABASE=
DB_USER=
DB_PASSWORD=
JWT_SECRET=
```
/database/.env
```
POSTGRES_DB=
POSTGRES_USER=
POSTGRES_PASSWORD=
PGADMIN_DEFAULT_EMAIL=
PGADMIN_DEFAULT_PASSWORD=
```

### Running up a Docker
1. Run ``docker-compose up`` in ``/database`` folder.
2. Run ``mvn clean install`` in ``/`` root folder
3. Run ``docker-compose up`` in ``/`` root folder.