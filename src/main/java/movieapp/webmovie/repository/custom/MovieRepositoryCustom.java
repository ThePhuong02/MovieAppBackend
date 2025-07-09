package movieapp.webmovie.repository.custom;

import movieapp.webmovie.entity.Movie;

import java.util.List;
import java.util.Map;

public interface MovieRepositoryCustom {
    List<Movie> findAll(Map<String, Object> params);
}
