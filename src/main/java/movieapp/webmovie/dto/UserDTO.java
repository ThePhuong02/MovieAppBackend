package movieapp.webmovie.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Integer userID;
    private String name;
    private String email;
    private String avatar;
    private String role;
}
