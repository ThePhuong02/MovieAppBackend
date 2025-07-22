package movieapp.webmovie.dto;

import lombok.Data;

@Data
public class SeriesDTO {
    private String title;
    private String description;
    private String poster;
    private Integer year;
}
