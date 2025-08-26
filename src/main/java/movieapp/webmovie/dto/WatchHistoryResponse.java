package movieapp.webmovie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WatchHistoryResponse {
    private Long historyId;
    private Long movieId;
    private String movieTitle;
    private String moviePoster;
    private LocalDateTime watchedAt;
    private Long watchedMinutes; // Số phút đã xem (từ lúc bắt đầu đến hiện tại)
}
