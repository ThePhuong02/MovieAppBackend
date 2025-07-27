package movieapp.webmovie.service;

import movieapp.webmovie.dto.MovieDTO;
import movieapp.webmovie.dto.MovieRequestDTO;
import movieapp.webmovie.entity.Genre;

import java.util.List;

public interface MovieService {
    List<MovieDTO> getAllMovies();

    MovieDTO getMovieById(Long id);

    MovieDTO createMovie(MovieRequestDTO dto);

    MovieDTO updateMovie(Long id, MovieRequestDTO dto);

    void deleteMovie(Long id);

    void assignGenresToMovie(Long movieId, List<Long> genreIds);

    void addGenreToMovie(Long movieId, Long genreId);

    void removeGenreFromMovie(Long movieId, Long genreId);

    List<Genre> getGenresByMovie(Long movieId);

    MovieDTO getMoviePlayInfo(Long movieId, Long userId);

    // ✅ mới
    List<MovieDTO> getMoviesByGenreId(Long genreId);
}
