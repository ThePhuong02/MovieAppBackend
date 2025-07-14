package movieapp.webmovie.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import movieapp.webmovie.entity.Series;

public interface SeriesRepository extends JpaRepository<Series, Integer> {}