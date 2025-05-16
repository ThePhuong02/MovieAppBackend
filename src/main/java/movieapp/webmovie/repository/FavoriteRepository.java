package movieapp.webmovie.repository;

import movieapp.webmovie.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    List<Favorite> findByUser_UserID(Integer userID);

    boolean existsByUser_UserIDAndMovie_MovieID(Integer userID, Integer movieID);
}
