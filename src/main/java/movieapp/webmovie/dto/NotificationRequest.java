package movieapp.webmovie.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private Long userId;
    private String title;
    private String message;
}