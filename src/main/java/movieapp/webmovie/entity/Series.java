package movieapp.webmovie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "series")
@Getter
@Setter
public class Series {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seriesID;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String poster;
    private Integer year;
}
