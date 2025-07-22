package movieapp.webmovie.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "WatchHistory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HistoryID")
    private Long historyId;

    @Column(name = "UserID", nullable = false)
    private Long userId;

    @Column(name = "MovieID", nullable = false)
    private Long movieId;

    @Column(name = "WatchedAt", columnDefinition = "DATETIME")
    private LocalDateTime watchedAt;

    @Column(name = "WatchedPercent")
    private Double watchedPercent;
}