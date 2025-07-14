package movieapp.webmovie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import movieapp.webmovie.entity.Genre;
import movieapp.webmovie.service.GenreService;
@RestController
@RequestMapping("/api/genres")
@PreAuthorize("hasRole('ADMIN')")
public class GenreController {
    
    @Autowired
    private GenreService genreService;

    @GetMapping
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable Integer id) {
        return genreService.getGenreById(id);
    }

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

@PutMapping("/{id}")
public ResponseEntity<?> updateGenre(@PathVariable Integer id, @RequestBody Genre genre) {
    try {
        Genre updated = genreService.updateGenre(id, genre);
        return ResponseEntity.ok(updated);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Update failed: " + e.getMessage());
    }
}


    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable Integer id) {
        genreService.deleteGenre(id);
    }
}
