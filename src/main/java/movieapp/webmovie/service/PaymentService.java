package movieapp.webmovie.service;

import movieapp.webmovie.dto.WebhookRequest;
import movieapp.webmovie.entity.Payment;

import java.util.List;

public interface PaymentService {
    String handleWebhook(WebhookRequest request);

    List<Payment> getUserPayments(Long userId);

    List<Payment> getAllPayments(); // ⬅️ Thêm cho admin xem tất cả
}
