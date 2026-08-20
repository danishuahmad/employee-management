# Employee Management API

A Spring Boot REST API for creating, reading, updating, and deleting employee records. Data is stored in an in-memory H2 database, making the application easy to run locally.

## Tech stack

- Java 25
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- H2 Database
- Maven and Lombok

## Requirements

- JDK 25

Maven is included through the Maven Wrapper, so a separate Maven installation is optional.

## Run locally

macOS/Linux:

```bash
./mvnw spring-boot:run
```

Windows:

```bat
mvnw.cmd spring-boot:run
```

The API starts at `http://localhost:8080`.

## API endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/employees` | List all employees |
| `GET` | `/employees/{id}` | Get one employee |
| `POST` | `/employees` | Create an employee |
| `PUT` | `/employees/{id}` | Update an employee |
| `DELETE` | `/employees/{id}` | Delete an employee |

### Example: create an employee

```bash
curl -X POST http://localhost:8080/employees \
  -H 'Content-Type: application/json' \
  -d '{"name":"Ada Lovelace","department":"Engineering","salary":95000}'
```

Example response (`201 Created`):

```json
{
  "id": 1,
  "name": "Ada Lovelace",
  "department": "Engineering",
  "salary": 95000
}
```

### Example: list employees

```bash
curl http://localhost:8080/employees
```

## H2 database console

While the application is running, open `http://localhost:8080/h2-console` and use:

| Setting | Value |
| --- | --- |
| JDBC URL | `jdbc:h2:mem:employeedb` |
| User Name | `sa` |
| Password | *(leave blank)* |

The database is in memory, so its data is cleared when the application stops.

## Tests

```bash
./mvnw test
```
