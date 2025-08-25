package movieapp.webmovie.service;

import movieapp.webmovie.entity.*;
import movieapp.webmovie.repository.*;
import movieapp.webmovie.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepo;

    public Rating rate(RatingRequest req) {
        Rating rating = new Rating();
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

    public Rating updateRating(Long ratingId, RatingRequest req) {
        Rating rating = ratingRepo.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found with id: " + ratingId));

        rating.setStars(req.getStars());
        rating.setComment(req.getComment());
        rating.setCreatedAt(LocalDateTime.now());
        return ratingRepo.save(rating);
    }

    public void deleteRating(Long ratingId) {
        if (!ratingRepo.existsById(ratingId)) {
            throw new RuntimeException("Rating not found with id: " + ratingId);
        }
        ratingRepo.deleteById(ratingId);
    }
}