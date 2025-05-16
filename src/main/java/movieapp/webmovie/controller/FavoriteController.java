package movieapp.webmovie.controller;

import movieapp.webmovie.dto.FavoriteDTO;
import movieapp.webmovie.entity.Favorite;
import movieapp.webmovie.entity.Movie;
import movieapp.webmovie.entity.User;
import movieapp.webmovie.repository.FavoriteRepository;
import movieapp.webmovie.repository.MovieRepository;
import movieapp.webmovie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/user/{userID}")
    public ResponseEntity<List<FavoriteDTO>> getFavoritesByUser(@PathVariable Integer userID) {
        List<FavoriteDTO> favorites = favoriteRepository.findByUser_UserID(userID)
                .stream()
                .map(fav -> new FavoriteDTO(
                        fav.getFavoriteID(),
                        fav.getUser().getUserID(),
                        fav.getMovie().getMovieID()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(favorites);
    }

    @PostMapping
    public ResponseEntity<String> addFavorite(@RequestBody FavoriteDTO favoriteDTO) {
        if (favoriteRepository.existsByUser_UserIDAndMovie_MovieID(favoriteDTO.getUserID(), favoriteDTO.getMovieID())) {
            return ResponseEntity.badRequest().body("Favorite already exists");
        }

        User user = userRepository.findById(favoriteDTO.getUserID()).orElse(null);
        Movie movie = movieRepository.findById(favoriteDTO.getMovieID()).orElse(null);

        if (user == null || movie == null) {
            return ResponseEntity.badRequest().body("Invalid user or movie ID");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setMovie(movie);
        favoriteRepository.save(favorite);

        return ResponseEntity.ok("Favorite added");
    }

    @DeleteMapping("/{favoriteID}")
    public ResponseEntity<String> deleteFavorite(@PathVariable Integer favoriteID) {
        if (!favoriteRepository.existsById(favoriteID)) {
            return ResponseEntity.badRequest().body("Favorite not found");
        }

        favoriteRepository.deleteById(favoriteID);
        return ResponseEntity.ok("Favorite removed");
    }
}
