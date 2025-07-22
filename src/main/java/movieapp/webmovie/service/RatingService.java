package movieapp.webmovie.service;

import movieapp.webmovie.entity.*;
import movieapp.webmovie.repository.*;
import movieapp.webmovie.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepo;

    public Rating rate(RatingRequest req) {
        Optional<Rating> existing = ratingRepo.findByUserIdAndMovieId(req.getUserId(), req.getMovieId());
        Rating rating = existing.orElse(new Rating());
        rating.setUserId(req.getUserId());
        rating.setMovieId(req.getMovieId());
        rating.setStars(req.getStars());
        rating.setComment(req.getComment());
        rating.setCreatedAt(LocalDateTime.now());
        return ratingRepo.save(rating);
    }

    public List<Rating> getRatings(Long movieId) {
        return ratingRepo.findByMovieId(movieId);
    }
}