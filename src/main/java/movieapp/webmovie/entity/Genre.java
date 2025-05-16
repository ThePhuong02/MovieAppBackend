package movieapp.webmovie.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Genres")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer genreID;

    private String name;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL)
    private List<Movie> movies;
}
