package movieapp.webmovie.service;

import movieapp.webmovie.dto.MovieDTO;
import movieapp.webmovie.dto.MovieRequestDTO;
import movieapp.webmovie.dto.MovieResponseDTO;

import java.util.List;
import java.util.Map;

public interface MovieService {
    List<MovieResponseDTO> findAll(Map<String, Object> params);
    List<MovieDTO> getAllMovies();

    MovieDTO getMovieById(Long id);

    MovieDTO createMovie(MovieRequestDTO dto);

    MovieDTO updateMovie(Long id, MovieRequestDTO dto);

    void deleteMovie(Long id);
}
