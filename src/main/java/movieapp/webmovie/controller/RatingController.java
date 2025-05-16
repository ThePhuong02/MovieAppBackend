package movieapp.webmovie.controller;

import movieapp.webmovie.dto.RatingDTO;
import movieapp.webmovie.entity.Movie;
import movieapp.webmovie.entity.Rating;
import movieapp.webmovie.entity.User;
import movieapp.webmovie.repository.MovieRepository;
import movieapp.webmovie.repository.RatingRepository;
import movieapp.webmovie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @PostMapping
    public ResponseEntity<String> addRating(@RequestBody RatingDTO dto) {
        User user = userRepository.findById(dto.getUserID()).orElse(null);
        Movie movie = movieRepository.findById(dto.getMovieID()).orElse(null);

        if (user == null || movie == null) {
            return ResponseEntity.badRequest().body("Invalid user or movie ID.");
        }

        if (ratingRepository.existsByUserAndMovie(user, movie)) {
            return ResponseEntity.badRequest().body("User has already rated this movie.");
        }

        Rating rating = Rating.builder()
                .user(user)
                .movie(movie)
                .stars(dto.getStars())
                .comment(dto.getComment())
                .createdAt(LocalDateTime.now())
                .build();

        ratingRepository.save(rating);
        return ResponseEntity.ok("Rating added successfully.");
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Rating>> getRatingsByMovie(@PathVariable Integer movieId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie == null)
            return ResponseEntity.notFound().build();

        List<Rating> ratings = ratingRepository.findByMovie(movie);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Rating>> getRatingsByUser(@PathVariable Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
            return ResponseEntity.notFound().build();

        List<Rating> ratings = ratingRepository.findByUser(user);
        return ResponseEntity.ok(ratings);
    }
}
