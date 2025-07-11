package movieapp.webmovie.service;

import movieapp.webmovie.dto.SubscriptionRequest;
import movieapp.webmovie.entity.Subscription;
import movieapp.webmovie.entity.Payment;

import java.util.Optional;

public interface SubscriptionService {
    String subscribe(SubscriptionRequest request);

    Optional<Subscription> getCurrentSubscription(Integer userId);

    // ✅ Thêm mới: tạo subscription từ đối tượng Payment (sau khi PayPal xác nhận
    // thành công)
    Subscription createSubscriptionFromPayment(Payment payment);
}
