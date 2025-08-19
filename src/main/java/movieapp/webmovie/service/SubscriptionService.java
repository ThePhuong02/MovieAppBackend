package movieapp.webmovie.service;

import movieapp.webmovie.dto.SubscriptionRequest;
import movieapp.webmovie.entity.Subscription;
import movieapp.webmovie.entity.Payment;
import java.util.Map;

public interface SubscriptionService {
    String subscribe(SubscriptionRequest request);

    Map<String, Object> getCurrentSubscription(Long userId);

    Subscription createSubscriptionFromPayment(Payment payment);

    boolean cancelSubscription(Long userId);

    boolean cancelAutoRenew(Long userId);

    // Kiểm tra user có quyền truy cập premium không
    boolean hasPremiumAccess(Long userId);

}
