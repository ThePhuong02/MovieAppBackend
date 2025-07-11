package movieapp.webmovie.entity;

import lombok.Data;
import movieapp.webmovie.enums.PaymentStatus;
import movieapp.webmovie.enums.PaymentType;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PaymentID")
    private Long paymentId;

    @Column(name = "UserID", nullable = false)
    private Long userId;

    @Column(name = "Amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "PaymentMethod", nullable = false)
    private String paymentMethod;

    @CreationTimestamp
    @Column(name = "PaidAt", columnDefinition = "DATETIME")
    private LocalDateTime paidAt;

    @Column(name = "TransactionRef", unique = true, nullable = false)
    private String transactionRef;

    @Enumerated(EnumType.STRING)
    @Column(name = "PaymentStatus", nullable = false)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "PaymentType", nullable = false)
    private PaymentType paymentType;
}
