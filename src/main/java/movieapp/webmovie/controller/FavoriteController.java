package movieapp.webmovie.controller;

import movieapp.webmovie.dto.FavoriteRequest;
import movieapp.webmovie.entity.*;
import movieapp.webmovie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    @Autowired
    private FavoriteService service;

    @PostMapping
    public ResponseEntity<String> addFavorite(@RequestBody FavoriteRequest request) {
        service.addFavorite(request.getUserId(), request.getMovieId());
        return ResponseEntity.ok("Added to favorite list.");
    }

    @DeleteMapping
    public ResponseEntity<String> removeFavorite(@RequestBody FavoriteRequest request) {
        service.removeFavorite(request.getUserId(), request.getMovieId());
        return ResponseEntity.ok("Removed from favorite list.");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Favorite>> getFavorites(@PathVariable Long userId) {
        List<Favorite> favorites = service.getFavorites(userId);
        return ResponseEntity.ok(favorites);
    }
}