package movieapp.webmovie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "Reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReportID")
    private Long reportId;

    @Column(name = "UserID")
    private Long userId;

    @Column(name = "MovieID")
    private Long movieId;

    @Column(name = "Content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "Status")
    private String status = "Chưa xử lý";

    @Column(name = "ReportedAt")
    private LocalDateTime reportedAt = LocalDateTime.now();

    @Column(name = "ProcessedAt")
    private LocalDateTime processedAt;

    @Column(name = "StaffID")
    private Long staffId;
}
