package movieapp.webmovie.dto;

import lombok.Data;
import java.util.List;

@Data
public class MovieGenreRequest {
    private Long movieId;
    private List<Long> genreIds;
}
