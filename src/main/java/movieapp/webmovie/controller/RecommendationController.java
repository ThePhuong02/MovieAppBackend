package movieapp.webmovie.controller;

import movieapp.webmovie.dto.RecommendationDTO;
import movieapp.webmovie.entity.Movie;
import movieapp.webmovie.entity.Recommendation;
import movieapp.webmovie.entity.User;
import movieapp.webmovie.repository.MovieRepository;
import movieapp.webmovie.repository.RecommendationRepository;
import movieapp.webmovie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping
    public List<RecommendationDTO> getAll() {
        return recommendationRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PostMapping
    public RecommendationDTO create(@RequestBody RecommendationDTO dto) {
        User user = userRepository.findById(dto.getUserID())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Movie movie = movieRepository.findById(dto.getMovieID())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Recommendation rec = new Recommendation();
        rec.setUser(user);
        rec.setMovie(movie);
        rec.setReason(dto.getReason());

        return toDTO(recommendationRepository.save(rec));
    }

    private RecommendationDTO toDTO(Recommendation rec) {
        return new RecommendationDTO(
                rec.getRecID(),
                rec.getUser().getUserID(),
                rec.getMovie().getMovieID(),
                rec.getReason());
    }
}
