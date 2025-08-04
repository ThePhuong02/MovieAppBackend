package movieapp.webmovie.controller;

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
    public ResponseEntity<String> addFavorite(@RequestParam Long userId, @RequestParam Long movieId) {
        service.addFavorite(userId, movieId);
        return ResponseEntity.ok("Đã thêm vào danh sách yêu thích.");
    }

    @DeleteMapping
    public void removeFavorite(@RequestParam Long userId, @RequestParam Long movieId) {
        service.removeFavorite(userId, movieId);
    }

    @GetMapping("/{userId}")
    public List<Favorite> getFavorites(@PathVariable Long userId) {
        return service.getFavorites(userId);
    }
}