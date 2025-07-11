package movieapp.webmovie.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "Plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PlanID")
    private Long planId;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Price", nullable = false)
    private BigDecimal price;

    @Column(name = "DurationDays", nullable = false)
    private Integer durationDays;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "GrantsPremiumAccess")
    private Boolean grantsPremiumAccess;
}
