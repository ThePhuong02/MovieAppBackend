package movieapp.webmovie.controller;

import jakarta.servlet.http.HttpServletRequest;
import movieapp.webmovie.entity.User;
import movieapp.webmovie.entity.WatchHistory;
import movieapp.webmovie.repository.WatchHistoryRepository;
import movieapp.webmovie.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class WatchHistoryController {

    @Autowired
    private WatchHistoryRepository watchHistoryRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // ✅ Lấy lịch sử xem phim của người dùng
    @GetMapping("/my")
    public ResponseEntity<List<WatchHistory>> getMyWatchHistory(HttpServletRequest request) {
        User user = jwtTokenUtil.getUserFromRequest(request);
        List<WatchHistory> historyList = watchHistoryRepository.findByUserId(user.getUserID());
        return ResponseEntity.ok(historyList);
    }
}
