package movieapp.webmovie.controller;

import java.util.List;
import movieapp.webmovie.dto.*;
import movieapp.webmovie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import movieapp.webmovie.entity.Genre;
import movieapp.webmovie.service.GenreService;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @Autowired
    private MovieService movieService;

    @GetMapping
    public ResponseEntity<List<GenreDTO>> getAllGenres() {
        List<GenreDTO> genres = genreService.getAllGenres().stream()
                .map(g -> new GenreDTO(g.getGenreID(), g.getName()))
                .toList();

        return ResponseEntity.ok(genres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGenreById(@PathVariable Long id) {
        Genre genre = genreService.getGenreById(id);
        if (genre == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(genre);
    }

    // ✅ Lọc phim theo thể loại (ai cũng gọi được)
    @GetMapping("/{id}/movies")
    public ResponseEntity<List<MovieDTO>> getMoviesByGenre(@PathVariable Long id) {
        List<MovieDTO> movies = movieService.getMoviesByGenreId(id);
        return ResponseEntity.ok(movies);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addGenre(@RequestBody Genre genre) {
        if (genre.getName() == null || genre.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Genre name must not be null or empty");
        }
        if (genre.getName().length() > 100) {
            return ResponseEntity.badRequest().body("Genre name must be less than 100 characters");
        }

        genreService.addGenre(genre);
        return ResponseEntity.ok(genre);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGenre(@PathVariable Long id, @RequestBody Genre genre) {
        try {
            Genre updated = genreService.updateGenre(id, genre);
            if (updated == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Update failed: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGenre(@PathVariable Long id) {
        Genre genre = genreService.getGenreById(id);
        if (genre == null) {
            return ResponseEntity.notFound().build();
        }
        genreService.deleteGenre(id);
        return ResponseEntity.ok("Deleted genre with ID: " + id);
    }
}
