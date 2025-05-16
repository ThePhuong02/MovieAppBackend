package movieapp.webmovie.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchHistoryDTO {
    private Integer historyID;
    private Integer userID;
    private Integer movieID;
    private LocalDateTime watchedAt;
}
