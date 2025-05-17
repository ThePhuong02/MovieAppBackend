package movieapp.webmovie.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import movieapp.webmovie.model.Movie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@Tag(name = "Movie", description = "Movie management APIs")
public class MovieController {

    private final List<Movie> movies = new ArrayList<>();

    @Operation(
        summary = "Get all movies",
        description = "Retrieve a list of all available movies"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "404", description = "No movies found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        if (movies.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Operation(
        summary = "Get a movie by ID",
        description = "Retrieve a specific movie by its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful operation", 
                    content = @Content(schema = @Schema(implementation = Movie.class))),
        @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return movies.stream()
                .filter(movie -> movie.getId().equals(id))
                .findFirst()
                .map(movie -> new ResponseEntity<>(movie, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
        summary = "Create a new movie",
        description = "Add a new movie to the database"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Movie created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        movies.add(movie);
        return new ResponseEntity<>(movie, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Update an existing movie",
        description = "Update a movie's information by its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Movie updated successfully"),
        @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie updatedMovie) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId().equals(id)) {
                updatedMovie.setId(id);
                movies.set(i, updatedMovie);
                return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(
        summary = "Delete a movie",
        description = "Delete a movie by its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Movie deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        boolean removed = movies.removeIf(movie -> movie.getId().equals(id));
        if (removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
