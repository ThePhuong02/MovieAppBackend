package movieapp.webmovie.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaybackLinkDTO {
    private String type; // "bunny-embed" | "direct"
    private String url; // URL để play (iframe hoặc direct)
}
