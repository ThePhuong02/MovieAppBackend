package movieapp.webmovie.repository;

import movieapp.webmovie.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByMovieId(Long movieId);

    Optional<Rating> findByUserIdAndMovieId(Long userId, Long movieId);
}
