package movieapp.webmovie.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDTO {
    private Integer favoriteID;
    private Integer userID;
    private Integer movieID;
}
