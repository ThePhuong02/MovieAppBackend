package movieapp.webmovie.controller;

import jakarta.servlet.http.HttpServletRequest;
import movieapp.webmovie.dto.ProgressRequest;
import movieapp.webmovie.dto.WatchHistoryResponse;
import movieapp.webmovie.entity.Movie;
import movieapp.webmovie.entity.User;
import movieapp.webmovie.entity.WatchHistory;
import movieapp.webmovie.repository.MovieRepository;
import movieapp.webmovie.repository.WatchHistoryRepository;
import movieapp.webmovie.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/history")
public class WatchHistoryController {

    @Autowired
    private WatchHistoryRepository watchHistoryRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // ✅ Lấy lịch sử xem phim
    @GetMapping("/my")
    public ResponseEntity<List<WatchHistoryResponse>> getMyWatchHistory(HttpServletRequest request) {
        User user = jwtTokenUtil.getUserFromRequest(request);
        List<WatchHistory> historyList = watchHistoryRepository.findByUserId(user.getUserID());

        List<WatchHistoryResponse> responseList = historyList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    // ✅ Cập nhật tiến độ xem (chỉ lưu %)
    @PatchMapping("/{movieId}/progress")
    public ResponseEntity<?> updateProgress(HttpServletRequest request,
            @PathVariable Long movieId,
            @RequestBody ProgressRequest req) {
        User user = jwtTokenUtil.getUserFromRequest(request);

        WatchHistory history = watchHistoryRepository.findByUserIdAndMovieId(user.getUserID(), movieId)
                .orElseGet(() -> WatchHistory.builder()
                        .userId(user.getUserID())
                        .movieId(movieId)
                        .build());

        history.setWatchedAt(LocalDateTime.now());
        history.setWatchedPercent(req.getPercent());

        watchHistoryRepository.save(history);
        return ResponseEntity.ok("Progress updated");
    }

    // ✅ Helper method để map entity → DTO
    private WatchHistoryResponse mapToResponse(WatchHistory history) {
        Movie movie = movieRepository.findById(history.getMovieId()).orElse(null);

        return WatchHistoryResponse.builder()
                .historyId(history.getHistoryId())
                .movieId(history.getMovieId())
                .movieTitle(movie != null ? movie.getTitle() : "Phim không tồn tại")
                .moviePoster(movie != null ? movie.getPoster() : null)
                .watchedAt(history.getWatchedAt())
                .watchedPercent(history.getWatchedPercent())
                .build();
    }
}
