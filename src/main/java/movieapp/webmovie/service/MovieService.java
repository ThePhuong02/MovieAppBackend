package movieapp.webmovie.service;

import movieapp.webmovie.dto.MovieDTO;
import movieapp.webmovie.dto.MovieRequestDTO;
import movieapp.webmovie.dto.PlaybackLinkDTO;
import movieapp.webmovie.entity.Genre;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    List<MovieDTO> getMoviesByGenreId(Long genreId);

    List<MovieDTO> getMoviesByGenreIds(List<Long> genreIds);

    // ✅ Trả về link phát (bunny/direct) – dùng bởi FE
    PlaybackLinkDTO getPlaybackLink(Long movieId);

    void streamVideo(Long movieId, HttpServletRequest request, HttpServletResponse response);
}
