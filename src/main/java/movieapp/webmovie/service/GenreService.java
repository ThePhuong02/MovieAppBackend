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

    public Genre getGenreById(Integer id) {
        return genreRepository.findById(id).orElse(null);
    }

    public Genre addGenre(Genre genre) {
        if (genre.getName() == null) {
        throw new IllegalArgumentException("Genre name is required");
    }
        if (genre.getName().length() > 100) {
            throw new IllegalArgumentException("Genre name must be less than 100 characters");
        }
        return

    genreRepository.save(genre);
    }

    public Genre updateGenre(Integer id, Genre updatedGenre) {
        Genre genre = genreRepository.findById(id).orElse(null);
        if (genre != null) {
            genre.setName(updatedGenre.getName());
            return genreRepository.save(genre);
        }
        return null;
    }

    public void deleteGenre(Integer id) {
        genreRepository.deleteById(id);
    }
}
