package movieapp.webmovie.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MovieArtists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieArtist {

  @EmbeddedId
  private MovieArtistId id = new MovieArtistId();

  @Column(length = 100)
  private String roleInMovie;

  // Mapping thủ công
  public void setMovieID(Long movieID) {
    this.id.setMovieID(movieID);
  }

  public void setArtistID(Long artistID) {
    this.id.setArtistID(artistID);
  }

  public Long getMovieID() {
    return this.id.getMovieID();
  }

  public Long getArtistID() {
    return this.id.getArtistID();
  }
}
