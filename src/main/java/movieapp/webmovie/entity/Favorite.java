package movieapp.webmovie.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Favorites")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer favoriteID;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "movieID", nullable = false)
    private Movie movie;
}
