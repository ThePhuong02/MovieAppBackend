package movieapp.webmovie.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Recommendations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recID;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "movieID", nullable = false)
    private Movie movie;

    @Column(columnDefinition = "TEXT")
    private String reason;
}
