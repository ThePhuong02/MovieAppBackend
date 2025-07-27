package movieapp.webmovie.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Supports")
public class Support {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supportID;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String message;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    private String response;

    @ManyToOne
    @JoinColumn(name = "respondedBy")
    private User respondedBy;

    private LocalDateTime respondedAt;

    @Column(name = "createdAt")
    private LocalDateTime createdAt = LocalDateTime.now();
}
