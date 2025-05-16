package movieapp.webmovie.repository;

import movieapp.webmovie.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {
    List<Recommendation> findByUser_UserID(Integer userID);

    List<Recommendation> findByMovie_MovieID(Integer movieID);
}
