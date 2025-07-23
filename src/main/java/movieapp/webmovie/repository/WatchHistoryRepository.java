package movieapp.webmovie.repository;

import movieapp.webmovie.entity.WatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {

    boolean existsByUserIdAndMovieId(Long userId, Long movieId);

    Optional<WatchHistory> findByUserIdAndMovieId(Long userId, Long movieId);

    // ✅ Thêm dòng này để dùng được trong WatchHistoryController
    List<WatchHistory> findByUserId(Long userId);
}
