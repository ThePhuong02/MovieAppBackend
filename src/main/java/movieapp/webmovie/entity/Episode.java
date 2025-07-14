package movieapp.webmovie.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "episodes")
@Getter
@Setter
public class Episode {
      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer episodeID;

    private String title;
    private String description;
    private Integer duration;
    private Integer episodeNumber;
    private String videoURL;

    @ManyToOne
    @JoinColumn(name = "seriesID")
    private Series series;
}
