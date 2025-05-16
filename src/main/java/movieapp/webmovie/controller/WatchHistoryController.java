package movieapp.webmovie.controller;

import movieapp.webmovie.dto.WatchHistoryDTO;
import movieapp.webmovie.entity.Movie;
import movieapp.webmovie.entity.User;
import movieapp.webmovie.entity.WatchHistory;
import movieapp.webmovie.repository.MovieRepository;
import movieapp.webmovie.repository.UserRepository;
import movieapp.webmovie.repository.WatchHistoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/watch-history")
public class WatchHistoryController {

    @Autowired
    private WatchHistoryRepository watchHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    // Get all watch history by user
    @GetMapping("/user/{userId}")
    public List<WatchHistoryDTO> getByUser(@PathVariable Integer userId) {
        return watchHistoryRepository.findByUser_UserID(userId)
                .stream()
                .map(history -> WatchHistoryDTO.builder()
                        .historyID(history.getHistoryID())
                        .userID(history.getUser().getUserID())
                        .movieID(history.getMovie().getMovieID())
                        .watchedAt(history.getWatchedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // Add new watch history
    @PostMapping
    public WatchHistoryDTO create(@RequestBody WatchHistoryDTO dto) {
        User user = userRepository.findById(dto.getUserID())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(dto.getMovieID())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        WatchHistory watchHistory = WatchHistory.builder()
                .user(user)
                .movie(movie)
                .watchedAt(dto.getWatchedAt() != null ? dto.getWatchedAt() : LocalDateTime.now())
                .build();

        WatchHistory saved = watchHistoryRepository.save(watchHistory);

        return WatchHistoryDTO.builder()
                .historyID(saved.getHistoryID())
                .userID(saved.getUser().getUserID())
                .movieID(saved.getMovie().getMovieID())
                .watchedAt(saved.getWatchedAt())
                .build();
    }
}
