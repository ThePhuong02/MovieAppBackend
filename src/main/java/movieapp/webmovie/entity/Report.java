package movieapp.webmovie.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private Integer reportID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", nullable = false)
    private User user; // User báo cáo hoặc người tạo báo cáo

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movieID", nullable = false)
    private Movie movie;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staffID", nullable = false)
    private User staff; // User với role = STAFF xử lý báo cáo

    @Column(length = 50)
    private String status;
}
