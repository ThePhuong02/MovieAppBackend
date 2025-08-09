package movieapp.webmovie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import movieapp.webmovie.enums.PaymentStatus;
import movieapp.webmovie.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentHistoryDTO {
    private Long paymentId;
    private Long userId;
    private BigDecimal amount;
    private String paymentMethod;
    private LocalDateTime paidAt;
    private String transactionRef;
    private PaymentStatus paymentStatus;
    private PaymentType paymentType;
    private Long planId;
    private String pricingId;

    // Thông tin hiển thị thêm cho FE
    private String title; // Free Plan / Premium Plan / Standard Plan / Plan name
    private String period; // "/year" nếu yearly, rỗng hoặc null nếu monthly/khác
}
