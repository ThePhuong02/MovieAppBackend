package movieapp.webmovie.controller;

import movieapp.webmovie.dto.MovieGenreRequest;
import movieapp.webmovie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movie-genres")
public class MovieGenreController {

    @Autowired
    private MovieService movieService;

    // ✅ Gán nhiều thể loại - chỉ admin
    @PostMapping("/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignGenresToMovie(@RequestBody MovieGenreRequest request) {
        try {
            movieService.assignGenresToMovie(request.getMovieId(), request.getGenreIds());
            return ResponseEntity.ok("Genres assigned to movie successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ Thêm 1 thể loại - chỉ admin
    @PostMapping("/{movieId}/add-genre/{genreId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addGenreToMovie(@PathVariable Long movieId, @PathVariable Long genreId) {
        movieService.addGenreToMovie(movieId, genreId);
        return ResponseEntity.ok("Genre added to movie.");
    }

    // ✅ Xoá 1 thể loại - chỉ admin
    @DeleteMapping("/{movieId}/remove-genre/{genreId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeGenreFromMovie(@PathVariable Long movieId, @PathVariable Long genreId) {
        movieService.removeGenreFromMovie(movieId, genreId);
        return ResponseEntity.ok("Genre removed from movie.");
    }

    // ✅ Lấy danh sách thể loại của 1 phim - CHO PHÉP USER
    @GetMapping("/{movieId}/genres")
    public ResponseEntity<?> getGenresByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(movieService.getGenresByMovie(movieId));
    }
}
