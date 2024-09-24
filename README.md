<p align="center">
  <a href="https://spring.io/projects/spring-boot" target="blank"><img src="https://miro.medium.com/v2/resize:fit:1400/format:webp/1*BBQq8yCFxaqneypPPpx2Jw.png" width="200" alt="Java Logo" /></a>
</p>


# Movie streaming - Backend

## Overview

The backend of my movie streaming project is responsible for handling all server-side operations, including user authentication, movie management, and API endpoints. This section provides an overview of the backend architecture, technologies used, and setup instructions.

## Technologies Used

- **Spring Boot 3**: A powerful framework for building Java-based applications.
- **Spring Security 6**: Provides authentication and authorization mechanisms for securing the application.
- **JWT Token Authentication**: Ensures secure communication between the client and server.
- **Spring Data JPA**: Simplifies data access and persistence using the Java Persistence API.
- **JSR-303 and Spring Validation**: Enables validation of objects based on annotations.
- **OpenAPI and Swagger UI Documentation**: Generates documentation for the API endpoints.
- **Docker**: Facilitates containerization of the backend application for deployment.
- **Instancio**: A powerful library for generating test data and objects, simplifying the creation of complex data structures during testing.
- **JUnit**: A widely-used testing framework for Java applications, providing a structured way to write and run repeatable tests.
- **AssertJ**: A fluent assertion library for Java, offering a rich and readable API for writing assertions in tests.
- **Mockito**: A popular mocking framework, enabling the creation of mock objects and verification of interactions in unit tests.


## Setup Instructions

To set up the backend of the Movie Streaming project, follow these steps:

1. Clone the repository:

```bash
   git clone https://github.com/Thompson6626/Movie-Streaming.git
```

2. Run the docker-compose file

```bash
  docker-compose up -d
```

3. Navigate to the movie-streaming directory:

```bash
  cd movie-streaming
```

4. Install dependencies:

```bash
  mvn clean install
```

4. Run the application but first replace the `x.x.x` with the current version from the `pom.xml` file

```bash
  java -jar target/movie-streaming-api-x.x.x.jar
```

5. Access the API documentation using Swagger UI:

Open a web browser and go to `http://localhost:8088/swagger-ui/index.html.


### Accessing Admin and Manager Endpoints

To use the admin and manager endpoints, follow these steps:

1. Send a request to the authentication endpoint:
2. Use the following JSON in the request body:
```json
   {
       "email": "adminemail@gmail.com",
       "password": "password1"
   }
```
3. This will return back a token you can use as the bearer token.
