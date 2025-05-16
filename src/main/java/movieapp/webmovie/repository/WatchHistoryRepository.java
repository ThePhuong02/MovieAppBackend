package movieapp.webmovie.repository;

import movieapp.webmovie.entity.WatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Integer> {
    List<WatchHistory> findByUser_UserID(Integer userID);
}
