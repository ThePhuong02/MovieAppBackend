package movieapp.webmovie.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationRequest {
    private Long userId;
    private String title;
    private String message;
}
