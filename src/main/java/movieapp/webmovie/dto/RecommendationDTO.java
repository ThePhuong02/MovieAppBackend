package movieapp.webmovie.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationDTO {
    private Integer recID;
    private Integer userID;
    private Integer movieID;
    private String reason;
}
