package movieapp.webmovie.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SubscriptionID")
    private Long subscriptionId;

    @Column(name = "UserID", nullable = false)
    private Long userId;

    @Column(name = "PlanID", nullable = true)
    private Long planId;

    @Column(name = "StartDate", columnDefinition = "DATETIME")
    private LocalDateTime startDate;

    @Column(name = "EndDate", columnDefinition = "DATETIME")
    private LocalDateTime endDate;

    @Column(name = "IsActive")
    private Boolean isActive;

    @Column(name = "PaymentID")
    private Long paymentId;

    @Column(name = "isCancelled")
    private Boolean isCancelled = false;

    @Column(name = "autoRenew")
    private Boolean autoRenew;

    @Column(name = "PricingId")
    private String pricingId;

}
