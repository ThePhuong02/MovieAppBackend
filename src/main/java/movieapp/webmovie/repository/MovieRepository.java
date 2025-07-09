package movieapp.webmovie.repository;

import movieapp.webmovie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    //List<Movie> findByPosterContainingIgnoreCase(String keyword);
}
