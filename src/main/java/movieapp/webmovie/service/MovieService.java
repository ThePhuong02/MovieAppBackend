package movieapp.webmovie.service;

import movieapp.webmovie.dto.MovieDTO;
import movieapp.webmovie.dto.MovieRequestDTO;

import java.util.List;

public interface MovieService {
    List<MovieDTO> getAllMovies();

    MovieDTO getMovieById(Long id);

    MovieDTO createMovie(MovieRequestDTO dto);

    MovieDTO updateMovie(Long id, MovieRequestDTO dto);

    void deleteMovie(Long id);
}
