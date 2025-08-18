package movieapp.webmovie.controller;

import java.util.List;
import java.util.stream.Collectors;

import movieapp.webmovie.dto.GenreDTO;
import movieapp.webmovie.dto.MovieDTO;
import movieapp.webmovie.entity.Genre;
import movieapp.webmovie.service.GenreService;
import movieapp.webmovie.service.MovieService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @Autowired
    private MovieService movieService;

    // ✅ Lấy tất cả thể loại
    @GetMapping
    public ResponseEntity<List<GenreDTO>> getAllGenres() {
        List<GenreDTO> genres = genreService.getAllGenres().stream()
                .map(g -> new GenreDTO(g.getGenreID(), g.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(genres);
    }

    // ✅ Lấy chi tiết 1 thể loại
    @GetMapping("/{id}")
    public ResponseEntity<?> getGenreById(@PathVariable Long id) {
        Genre genre = genreService.getGenreById(id);
        if (genre == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new GenreDTO(genre.getGenreID(), genre.getName()));
    }

    // ✅ Lọc phim theo thể loại (public API)
    @GetMapping("/{id}/movies")
    public ResponseEntity<List<MovieDTO>> getMoviesByGenre(@PathVariable Long id) {
        List<MovieDTO> movies = movieService.getMoviesByGenreId(id);
        return ResponseEntity.ok(movies);
    }

    // ✅ Thêm thể loại
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addGenre(@RequestBody Genre genre) {
        if (genre.getName() == null || genre.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Genre name must not be null or empty");
        }
        if (genre.getName().length() > 100) {
            return ResponseEntity.badRequest().body("Genre name must be less than 100 characters");
        }

        Genre saved = genreService.addGenre(genre);
        return ResponseEntity.ok(new GenreDTO(saved.getGenreID(), saved.getName()));
    }

    // ✅ Cập nhật thể loại
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGenre(@PathVariable Long id, @RequestBody Genre genre) {
        try {
            Genre updated = genreService.updateGenre(id, genre);
            if (updated == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(new GenreDTO(updated.getGenreID(), updated.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Update failed: " + e.getMessage());
        }
    }

    // ✅ Xóa thể loại
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
