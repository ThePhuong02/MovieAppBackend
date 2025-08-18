package movieapp.webmovie.service;

import movieapp.webmovie.dto.MovieDTO;
import movieapp.webmovie.dto.MovieRequestDTO;
import movieapp.webmovie.entity.Genre;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public interface MovieService {

    // CRUD Movie
    List<MovieDTO> getAllMovies();

    MovieDTO getMovieById(Long id);

    MovieDTO createMovie(MovieRequestDTO dto);

    MovieDTO updateMovie(Long id, MovieRequestDTO dto);

    void deleteMovie(Long id);

    // Quản lý thể loại cho phim
    void assignGenresToMovie(Long movieId, List<Long> genreIds);

    void addGenreToMovie(Long movieId, Long genreId);

    void removeGenreFromMovie(Long movieId, Long genreId);

    List<Genre> getGenresByMovie(Long movieId);

    // Lấy thông tin play (có kiểm tra Premium)
    MovieDTO getMoviePlayInfo(Long movieId, Long userId);

    // Lọc theo thể loại
    List<MovieDTO> getMoviesByGenreId(Long genreId);

    List<MovieDTO> getMoviesByGenreIds(List<Long> genreIds);

    // Stream video
    void streamVideo(Long movieId, HttpServletRequest request, HttpServletResponse response);
}
