package movieapp.webmovie.dto;

import lombok.Data;

@Data
public class SupportRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String message;
    private Long userId;
}
