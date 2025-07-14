package movieapp.webmovie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import movieapp.webmovie.dto.AssignArtistRequest;
import movieapp.webmovie.entity.MovieArtist;
import movieapp.webmovie.entity.MovieArtistId;
import movieapp.webmovie.repository.MovieArtistRepository;

@RestController
@RequestMapping("/api/movie-artists")
@PreAuthorize("hasRole('ADMIN')")
public class MovieArtistController {
  
    @Autowired
    private MovieArtistRepository movieArtistRepo;

@PostMapping("/assign")
public ResponseEntity<?> assignArtistToMovie(@RequestBody AssignArtistRequest request) {
    System.out.println("MovieID: " + request.getMovieID());
    System.out.println("ArtistID: " + request.getArtistID());

    MovieArtistId id = new MovieArtistId(request.getMovieID(), request.getArtistID());

    if (movieArtistRepo.existsById(id)) {
        return ResponseEntity.badRequest().body("Artist already assigned to this movie.");
    }

    MovieArtist ma = new MovieArtist();
    ma.setMovieID(request.getMovieID());
    ma.setArtistID(request.getArtistID());
    ma.setRoleInMovie(request.getRoleInMovie());

    movieArtistRepo.save(ma);
    return ResponseEntity.ok("Artist assigned successfully.");
}

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<MovieArtist>> getArtistsForMovie(@PathVariable Integer movieId) {
        return ResponseEntity.ok(movieArtistRepo.findByMovieID(movieId));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeArtistFromMovie(@RequestBody AssignArtistRequest request) {
        MovieArtistId id = new MovieArtistId(request.getMovieID(), request.getArtistID());

        if (!movieArtistRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        movieArtistRepo.deleteById(id);
        return ResponseEntity.ok("Artist removed from movie.");
    }
}
