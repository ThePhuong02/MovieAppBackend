package movieapp.webmovie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import movieapp.webmovie.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
