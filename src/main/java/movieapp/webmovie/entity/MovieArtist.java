package movieapp.webmovie.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "movieartists")
@IdClass(MovieArtistId.class)
public class MovieArtist {
  @Id
    private Integer movieID;

    @Id
    private Integer artistID;

    private String roleInMovie;

    @ManyToOne
    @JoinColumn(name = "movieID", insertable = false, updatable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "artistID", insertable = false, updatable = false)
    private Artist artist;
}

