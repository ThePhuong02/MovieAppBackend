package movieapp.webmovie.controller;

import jakarta.servlet.http.HttpServletRequest;
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
import java.time.temporal.ChronoUnit;
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

    // ✅ Lấy lịch sử xem phim của người dùng
    @GetMapping("/my")
    public ResponseEntity<List<WatchHistoryResponse>> getMyWatchHistory(HttpServletRequest request) {
        User user = jwtTokenUtil.getUserFromRequest(request);
        List<WatchHistory> historyList = watchHistoryRepository.findByUserId(user.getUserID());

        // ✅ Map entity thành response DTO với thông tin phim
        List<WatchHistoryResponse> responseList = historyList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    // ✅ Helper method để map entity thành DTO
    private WatchHistoryResponse mapToResponse(WatchHistory history) {
        Movie movie = movieRepository.findById(history.getMovieId()).orElse(null);

        // ✅ Tính số phút đã xem từ watchedAt đến hiện tại
        long watchedMinutes = 0;
        if (history.getWatchedAt() != null) {
            watchedMinutes = ChronoUnit.MINUTES.between(history.getWatchedAt(), LocalDateTime.now());
            // Nếu âm thì set về 0 (trường hợp lỗi thời gian)
            watchedMinutes = Math.max(0, watchedMinutes);
        }

        return WatchHistoryResponse.builder()
                .historyId(history.getHistoryId())
                .movieId(history.getMovieId())
                .movieTitle(movie != null ? movie.getTitle() : "Phim không tồn tại")
                .moviePoster(movie != null ? movie.getPoster() : null)
                .watchedAt(history.getWatchedAt())
                .watchedMinutes(watchedMinutes)
                .build();
    }
}
