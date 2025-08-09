package movieapp.webmovie.dto;

import lombok.Data;

@Data
public class SubscriptionRequest {
    private Long userId;
    private Long planId;
    private String paymentMethod;
    // Cho UI mới: id gói từ /api/plans/pricing (ví dụ: premium-monthly,
    // standard-yearly)
    private String pricingId;
}
