package movieapp.webmovie.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "UserDevices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", referencedColumnName = "userID")
    private User user;

    private String deviceName;
    private String ipAddress;
    private String loginToken;
    private LocalDateTime lastActive = LocalDateTime.now();
    private boolean isRevoked = false;
}