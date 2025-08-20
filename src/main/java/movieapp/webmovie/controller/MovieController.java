package movieapp.webmovie.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import movieapp.webmovie.dto.MovieDTO;
import movieapp.webmovie.dto.MovieRequestDTO;
import movieapp.webmovie.dto.PlaybackLinkDTO;
import movieapp.webmovie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieDTO> createMovie(@RequestBody MovieRequestDTO movieRequest) {
        return ResponseEntity.ok(movieService.createMovie(movieRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieDTO> updateMovie(
            @PathVariable Long id,
            @RequestBody MovieRequestDTO movieRequest) {
        return ResponseEntity.ok(movieService.updateMovie(id, movieRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Link phát cho FE (ưu tiên Bunny Embed)
    @GetMapping("/{id}/play")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PlaybackLinkDTO> getPlaybackLink(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getPlaybackLink(id));
    }

    // ✅ Stream direct (Dropbox/local) – dùng khi không có playbackId
    @GetMapping("/{id}/stream")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void streamMovie(
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        movieService.streamVideo(id, request, response);
    }
}
