package movieapp.webmovie.controller;

import movieapp.webmovie.dto.MovieDTO;
import movieapp.webmovie.entity.Genre;
import movieapp.webmovie.entity.Movie;
import movieapp.webmovie.repository.GenreRepository;
import movieapp.webmovie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable Integer id) {
        return movieRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Movie createMovie(@RequestBody MovieDTO movieDTO) {
        Genre genre = genreRepository.findById(movieDTO.getGenreID()).orElse(null);

        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setDescription(movieDTO.getDescription());
        movie.setDuration(movieDTO.getDuration());
        movie.setYear(movieDTO.getYear());
        movie.setPoster(movieDTO.getPoster());
        movie.setGenre(genre);

        return movieRepository.save(movie);
    }

    @PutMapping("/{id}")
    public Movie updateMovie(@PathVariable Integer id, @RequestBody MovieDTO movieDTO) {
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie != null) {
            Genre genre = genreRepository.findById(movieDTO.getGenreID()).orElse(null);
            movie.setTitle(movieDTO.getTitle());
            movie.setDescription(movieDTO.getDescription());
            movie.setDuration(movieDTO.getDuration());
            movie.setYear(movieDTO.getYear());
            movie.setPoster(movieDTO.getPoster());
            movie.setGenre(genre);
            return movieRepository.save(movie);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Integer id) {
        movieRepository.deleteById(id);
    }
}
