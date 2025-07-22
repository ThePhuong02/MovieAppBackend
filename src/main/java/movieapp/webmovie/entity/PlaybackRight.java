package movieapp.webmovie.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PlaybackRights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaybackRight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rightId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long movieId;

    private LocalDateTime expireAt;

    private Boolean canDownload;

    private LocalDateTime purchasedAt;

    private Long paymentId;
}
