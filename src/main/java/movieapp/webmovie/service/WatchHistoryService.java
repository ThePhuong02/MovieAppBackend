package movieapp.webmovie.service;

public interface WatchHistoryService {
    void logWatchHistory(Long userId, Long movieId, Double watchedPercent);
}
