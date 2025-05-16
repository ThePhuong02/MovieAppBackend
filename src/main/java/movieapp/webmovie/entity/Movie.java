package movieapp.webmovie.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieID;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer duration;

    private Integer year;

    private String poster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genreID")
    private Genre genre;
}
