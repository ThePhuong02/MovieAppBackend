package movieapp.webmovie.entity;

import jakarta.persistence.*;
import lombok.*;
import movieapp.webmovie.enums.Role;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String phone;

    private String avatar;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "reset_token")
    private String resetToken;
}
