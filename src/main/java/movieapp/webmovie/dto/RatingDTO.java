package movieapp.webmovie.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingDTO {
    private Integer userID;
    private Integer movieID;
    private Integer stars;
    private String comment;
}
