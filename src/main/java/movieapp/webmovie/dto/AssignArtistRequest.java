package movieapp.webmovie.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignArtistRequest {
    private Integer movieID;
    private Integer artistID;
    private String roleInMovie;
}
