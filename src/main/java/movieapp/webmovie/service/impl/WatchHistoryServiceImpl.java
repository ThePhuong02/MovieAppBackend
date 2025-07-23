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
    public void logWatchHistory(Long userId, Long movieId, Double watchedPercent) {
        WatchHistory history = WatchHistory.builder()
                .userId(userId)
                .movieId(movieId)
                .watchedAt(LocalDateTime.now())
                .watchedPercent(watchedPercent)
                .build();
        watchHistoryRepository.save(history);
    }
}
