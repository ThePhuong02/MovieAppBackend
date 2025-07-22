package movieapp.webmovie.controller;

import java.util.List;

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

    // ✅ Cho phép tất cả user (kể cả chưa login) xem thể loại
    @GetMapping
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    // ✅ Cho phép xem chi tiết thể loại
    @GetMapping("/{id}")
    public ResponseEntity<?> getGenreById(@PathVariable Long id) {
        Genre genre = genreService.getGenreById(id);
        if (genre == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(genre);
    }

    // ✅ Chỉ ADMIN được thêm thể loại
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

    // ✅ Chỉ ADMIN được sửa
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

    // ✅ Chỉ ADMIN được xóa
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
