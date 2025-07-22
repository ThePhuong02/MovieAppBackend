package movieapp.webmovie.service.impl;

import movieapp.webmovie.dto.MovieDTO;
import movieapp.webmovie.dto.MovieRequestDTO;
import movieapp.webmovie.entity.Genre;
import movieapp.webmovie.entity.Movie;
import movieapp.webmovie.repository.GenreRepository;
import movieapp.webmovie.repository.MovieRepository;
import movieapp.webmovie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    private MovieDTO convertToDTO(Movie movie) {
        return MovieDTO.builder()
                .movieID(movie.getMovieID())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .duration(movie.getDuration())
                .year(movie.getYear())
                .poster(movie.getPoster())
                .accessLevel(movie.getAccessLevel())
                .trailerURL(movie.getTrailerURL())
                .videoURL(movie.getVideoURL())
                .build();
    }

    private void updateEntity(Movie movie, MovieRequestDTO dto) {
        movie.setTitle(dto.getTitle());
        movie.setDescription(dto.getDescription());
        movie.setDuration(dto.getDuration());
        movie.setYear(dto.getYear());
        movie.setPoster(dto.getPoster());
        movie.setAccessLevel(dto.getAccessLevel());
        movie.setTrailerURL(dto.getTrailerURL());
        movie.setVideoURL(dto.getVideoURL());
    }

    @Override
    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MovieDTO getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
        return convertToDTO(movie);
    }

    @Override
    public MovieDTO createMovie(MovieRequestDTO dto) {
        Movie movie = new Movie();
        updateEntity(movie, dto);
        return convertToDTO(movieRepository.save(movie));
    }

    @Override
    public MovieDTO updateMovie(Long id, MovieRequestDTO dto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
        updateEntity(movie, dto);
        return convertToDTO(movieRepository.save(movie));
    }

    @Override
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new RuntimeException("Movie not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }

    @Override
    public void assignGenresToMovie(Long movieId, List<Long> genreIds) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + movieId));

        List<Genre> genres = genreRepository.findAllById(genreIds);
        if (genres.isEmpty()) {
            throw new IllegalArgumentException("No valid genres found.");
        }

        movie.setGenres(new HashSet<>(genres));
        movieRepository.save(movie);
    }

    @Override
    public void addGenreToMovie(Long movieId, Long genreId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));

        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new IllegalArgumentException("Genre not found"));

        movie.getGenres().add(genre);
        movieRepository.save(movie);
    }

    @Override
    public void removeGenreFromMovie(Long movieId, Long genreId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));

        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new IllegalArgumentException("Genre not found"));

        movie.getGenres().remove(genre);
        movieRepository.save(movie);
    }

    @Override
    public List<Genre> getGenresByMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));
        return new ArrayList<>(movie.getGenres());
    }
}
