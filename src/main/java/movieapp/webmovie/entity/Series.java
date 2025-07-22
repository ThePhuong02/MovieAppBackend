package movieapp.webmovie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "series")
@Getter
@Setter
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seriesID;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String poster;
    private Integer year;
}
