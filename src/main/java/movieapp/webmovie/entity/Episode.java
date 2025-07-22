package movieapp.webmovie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "episodes")
@Getter
@Setter
public class Episode {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long episodeID;

  private String title;
  private String description;
  private Integer duration;
  private Integer episodeNumber;
  private String videoURL;

  @ManyToOne
  @JoinColumn(name = "SeriesID")
  private Series series;
}
