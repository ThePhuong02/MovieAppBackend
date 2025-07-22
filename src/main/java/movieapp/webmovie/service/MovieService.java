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

    // Gán nhiều thể loại cho phim
    void assignGenresToMovie(Long movieId, List<Long> genreIds);

    // Thêm 1 thể loại cho phim
    void addGenreToMovie(Long movieId, Long genreId);

    // Gỡ 1 thể loại khỏi phim
    void removeGenreFromMovie(Long movieId, Long genreId);

    // Lấy tất cả thể loại của 1 phim
    List<Genre> getGenresByMovie(Long movieId);
}
