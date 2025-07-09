package movieapp.webmovie.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieResponseDTO {
    private Long id;
    private String description;
    private String title;
}
