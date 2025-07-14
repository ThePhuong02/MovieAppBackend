package movieapp.webmovie.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieArtistId implements Serializable {

     private Integer movieID;
    private Integer artistID;

}
