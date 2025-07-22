package movieapp.webmovie.entity;

import jakarta.persistence.*;
import lombok.*;
import movieapp.webmovie.enums.AccessLevel;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "genres")
@ToString(exclude = "genres")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MovieID")
    private Long movieID;

    @Column(name = "Title", nullable = false)
    private String title;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "Duration")
    private Integer duration; // minutes

    @Column(name = "Year")
    private Integer year;

    @Column(name = "Poster")
    private String poster;

    @Enumerated(EnumType.STRING)
    @Column(name = "AccessLevel")
    private AccessLevel accessLevel;

    @Column(name = "TrailerURL")
    private String trailerURL;

    @Column(name = "VideoURL")
    private String videoURL;

    @ManyToMany
    @JoinTable(name = "MovieGenres", joinColumns = @JoinColumn(name = "MovieID"), inverseJoinColumns = @JoinColumn(name = "GenreID"))
    private Set<Genre> genres = new HashSet<>();
}
