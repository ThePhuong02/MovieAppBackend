package movieapp.webmovie.dto;

import lombok.Data;

@Data
public class RatingRequest {
    private Long userId;
    private Long movieId;
    private int stars;
    private String comment;
}