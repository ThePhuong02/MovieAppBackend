package movieapp.webmovie.entity;

import jakarta.persistence.*;
import lombok.*;
import movieapp.webmovie.enums.AccessLevel;

@Entity
@Table(name = "Movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MovieID")
    private Long movieID;

    @Column(name = "Title")
    private String title;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "Duration")
    private Integer duration;

    @Column(name = "Year")
    private Integer year;

    @Column(name = "Poster")
    private String poster;

    @Enumerated(EnumType.STRING)
    @Column(name = "AccessLevel", nullable = false)
    private AccessLevel accessLevel;

    @Column(name = "TrailerURL")
    private String trailerURL;

    @Column(name = "VideoURL")
    private String videoURL;
}
