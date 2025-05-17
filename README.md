# Movie App Backend

A Spring Boot application for managing movies with REST API and Swagger documentation.

## Prerequisites

- Java 17
- Maven
- MySQL
- Node.js and npm (for Prettier)

## Setup

1. Clone the repository
2. Create a MySQL database named `movieapp`
3. Configure database connection in `src/main/resources/application.properties` if needed
4. Install Prettier dependencies:

```bash
npm install
```

## Running the Application

```bash
mvn spring-boot:run
```

The application will start on port 8080.

## API Documentation

Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

API documentation in JSON format:

```
http://localhost:8080/api-docs
```

## Code Formatting

Format code using Prettier:

```bash
npm run format
```

Check code formatting:

```bash
npm run format:check
```

## API Endpoints

- `GET /api/movies` - Get all movies
- `GET /api/movies/{id}` - Get a movie by ID
- `POST /api/movies` - Create a new movie
- `PUT /api/movies/{id}` - Update a movie
- `DELETE /api/movies/{id}` - Delete a movie

## Sample Movie JSON

```json
{
  "id": 1,
  "title": "The Shawshank Redemption",
  "description": "Two imprisoned men bond over a number of years...",
  "year": 1994,
  "duration": 142,
  "poster": "https://example.com/poster.jpg",
  "genre": "Drama"
}
``` 