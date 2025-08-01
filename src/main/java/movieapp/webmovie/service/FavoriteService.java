package movieapp.webmovie.service;

import movieapp.webmovie.entity.*;
import movieapp.webmovie.repository.*;
import movieapp.webmovie.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepo;

    public void addFavorite(Long userId, Long movieId) {
        if (!favoriteRepo.existsByUserIdAndMovieId(userId, movieId)) {
            favoriteRepo.save(Favorite.builder().userId(userId).movieId(movieId).build());
        }
    }

    public void removeFavorite(Long userId, Long movieId) {
        favoriteRepo.deleteByUserIdAndMovieId(userId, movieId);
    }

    public List<Favorite> getFavorites(Long userId) {
        return favoriteRepo.findByUserId(userId);
    }
}