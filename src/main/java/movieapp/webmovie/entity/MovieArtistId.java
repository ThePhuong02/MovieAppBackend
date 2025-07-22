package movieapp.webmovie.entity;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieArtistId implements Serializable {
    private Long movieID;
    private Long artistID;
}
