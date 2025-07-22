package movieapp.webmovie.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignArtistRequest {
    private Long movieID;
    private Long artistID;
    private String roleInMovie;
}
