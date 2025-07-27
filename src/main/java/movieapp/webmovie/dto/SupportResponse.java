package movieapp.webmovie.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SupportResponse {
    private Long supportID;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String message;
    private String response;
    private String respondedByName;
    private LocalDateTime respondedAt;
    private LocalDateTime createdAt;
}
