package movieapp.webmovie.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import movieapp.webmovie.entity.Genre;
import movieapp.webmovie.repository.GenreRepository;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Genre getGenreById(Long id) {
        return genreRepository.findById(id).orElse(null);
    }

    public Genre addGenre(Genre genre) {
        if (genre.getName() == null || genre.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Genre name is required");
        }
        if (genre.getName().length() > 100) {
            throw new IllegalArgumentException("Genre name must be less than 100 characters");
        }
        return genreRepository.save(genre);
    }

    public Genre updateGenre(Long id, Genre updatedGenre) {
        Genre genre = genreRepository.findById(id).orElse(null);
        if (genre != null) {
            genre.setName(updatedGenre.getName());
            return genreRepository.save(genre);
        }
        throw new IllegalArgumentException("Genre not found with ID: " + id);
    }

    public void deleteGenre(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new IllegalArgumentException("Genre not found with ID: " + id);
        }
        genreRepository.deleteById(id);
    }
}
