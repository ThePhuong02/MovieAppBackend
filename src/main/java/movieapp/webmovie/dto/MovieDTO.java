package movieapp.webmovie.dto;

import lombok.*;
import movieapp.webmovie.enums.AccessLevel;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTO {
    private Long movieID;
    private String title;
    private String description;
    private Integer duration;
    private Integer year;
    private String poster;
    private AccessLevel accessLevel;
    private String trailerURL;
    private String videoURL;
    private List<GenreDTO> genres; // Thêm field này
}
