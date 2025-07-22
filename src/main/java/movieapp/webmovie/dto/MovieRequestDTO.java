package movieapp.webmovie.dto;

import lombok.*;
import movieapp.webmovie.enums.AccessLevel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieRequestDTO {
    private String title;
    private String description;
    private Integer duration;
    private Integer year;
    private String poster;
    private AccessLevel accessLevel;
    private String trailerURL;
    private String videoURL; // ⬅ Cloudinary video URL dán vào đây
}
