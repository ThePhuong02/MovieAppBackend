package movieapp.webmovie.repository;

import movieapp.webmovie.entity.Rating;
import movieapp.webmovie.entity.Movie;
import movieapp.webmovie.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    List<Rating> findByMovie(Movie movie);

    List<Rating> findByUser(User user);

    boolean existsByUserAndMovie(User user, Movie movie);
}