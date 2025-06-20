package movieapp.webmovie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private Integer userID;
    private String name;
    private String email;
    private String phone;
    private String avatar;
    private String role;
}
