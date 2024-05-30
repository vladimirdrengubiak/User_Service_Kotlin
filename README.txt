User Service

This is a simple user management system using Spring Boot and PostgreSQL. It allows adding, viewing, updating, and deleting users.

Prerequisites:
- Java 17 or higher
- Docker

To build and start the application:

./mvnw clean

./mvnw package -DskipTests

docker-compose up --build

API endpoints:

- Get all users: GET /api/users
- Create a user: POST /api/users
- Get a user by ID: GET /api/users/{id}
- Update a user: PUT /api/users/{id}
- Delete a user: DELETE /api/users/{id}

This application includes automated API documentation using SpringDoc's OpenAPI UI and can be accessed at:

http://localhost:8080/swagger-ui.html