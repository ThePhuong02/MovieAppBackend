package movieapp.webmovie.controller;

import movieapp.webmovie.dto.PagedMovieResponse;
import movieapp.webmovie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movies")
public class SearchController {

    @Autowired
    private MovieService movieService;

    // ✅ API tìm kiếm phim theo spec SEARCH_API_GUIDE.md
    @GetMapping("/search")
    public ResponseEntity<?> searchMovies(
            @RequestParam("query") String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        // Kiểm tra tham số query
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Query parameter is required.");
        }

        // Kiểm tra giá trị page và limit
        if (page < 1) {
            page = 1;
        }
        if (limit < 1) {
            limit = 10;
        }

        try {
            PagedMovieResponse result = movieService.searchMovies(query, page, limit);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while searching: " + e.getMessage());
        }
    }
}
