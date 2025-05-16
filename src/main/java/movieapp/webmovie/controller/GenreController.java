package movieapp.webmovie.controller;

import movieapp.webmovie.dto.GenreDTO;
import movieapp.webmovie.entity.Genre;
import movieapp.webmovie.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    @Autowired
    private GenreRepository genreRepository;

    // Get all genres
    @GetMapping
    public List<GenreDTO> getAllGenres() {
        return genreRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get genre by ID
    @GetMapping("/{id}")
    public GenreDTO getGenreById(@PathVariable Integer id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found"));
        return convertToDTO(genre);
    }

    // Create new genre
    @PostMapping
    public GenreDTO createGenre(@RequestBody GenreDTO genreDTO) {
        Genre genre = Genre.builder()
                .name(genreDTO.getName())
                .build();
        genre = genreRepository.save(genre);
        return convertToDTO(genre);
    }

    // Update existing genre
    @PutMapping("/{id}")
    public GenreDTO updateGenre(@PathVariable Integer id, @RequestBody GenreDTO genreDTO) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found"));
        genre.setName(genreDTO.getName());
        genre = genreRepository.save(genre);
        return convertToDTO(genre);
    }

    // Delete genre
    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable Integer id) {
        genreRepository.deleteById(id);
    }

    // Helper method to convert Entity to DTO
    private GenreDTO convertToDTO(Genre genre) {
        return GenreDTO.builder()
                .genreID(genre.getGenreID())
                .name(genre.getName())
                .build();
    }
}
