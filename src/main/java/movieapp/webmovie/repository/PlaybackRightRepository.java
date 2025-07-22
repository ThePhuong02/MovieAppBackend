package movieapp.webmovie.repository;

import movieapp.webmovie.entity.PlaybackRight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PlaybackRightRepository extends JpaRepository<PlaybackRight, Long> {
    List<PlaybackRight> findByUserIdAndExpireAtAfter(Long userId, LocalDateTime now);

    boolean existsByUserIdAndMovieIdAndExpireAtAfter(Long userId, Long movieId, LocalDateTime now);

    Optional<PlaybackRight> findByUserIdAndMovieIdAndExpireAtAfter(Long userId, Long movieId, LocalDateTime now);
}
