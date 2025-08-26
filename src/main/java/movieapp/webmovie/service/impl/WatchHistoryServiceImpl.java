package movieapp.webmovie.service.impl;

import movieapp.webmovie.entity.WatchHistory;
import movieapp.webmovie.repository.WatchHistoryRepository;
import movieapp.webmovie.service.WatchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WatchHistoryServiceImpl implements WatchHistoryService {

    @Autowired
    private WatchHistoryRepository watchHistoryRepository;

    @Override
    public void logWatchHistory(Long userId, Long movieId) {
        // ✅ Kiểm tra xem đã có lịch sử xem chưa
        WatchHistory history = watchHistoryRepository.findByUserIdAndMovieId(userId, movieId)
                .orElse(WatchHistory.builder()
                        .userId(userId)
                        .movieId(movieId)
                        .watchedPercent(0.0) // Set default
                        .build());

        // ✅ Chỉ cập nhật thời gian xem (lần cuối xem)
        history.setWatchedAt(LocalDateTime.now());

        watchHistoryRepository.save(history);
    }
}
