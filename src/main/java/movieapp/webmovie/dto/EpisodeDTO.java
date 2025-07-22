package movieapp.webmovie.dto;

import lombok.Data;

@Data
public class EpisodeDTO {
    private String title;
    private String description;
    private Integer duration;
    private Integer episodeNumber;
    private String videoURL;
    private Long seriesID;
}
