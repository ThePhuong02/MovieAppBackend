package movieapp.webmovie.dto;

import lombok.Data;

@Data
public class SubscriptionRequest {
    private Long userId;
    private Long planId;
    private String paymentMethod;
}
