package movieapp.webmovie.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private Integer movieID;
    private String title;
    private String description;
    private Integer duration;
    private Integer year;
    private String poster;
    private Integer genreID;
}
