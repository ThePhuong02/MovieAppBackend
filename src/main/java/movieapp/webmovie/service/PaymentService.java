package movieapp.webmovie.service;

import movieapp.webmovie.dto.PaymentHistoryDTO;
import movieapp.webmovie.dto.WebhookRequest;
import movieapp.webmovie.entity.Movie;
import movieapp.webmovie.entity.Payment;
import movieapp.webmovie.entity.User;

import java.util.List;

public interface PaymentService {

    // Xử lý callback từ cổng thanh toán
    String handleWebhook(WebhookRequest request);

    // Truy vấn lịch sử thanh toán theo người dùng
    List<PaymentHistoryDTO> getUserPayments(Long userId);

    // Quản trị viên xem tất cả thanh toán
    List<PaymentHistoryDTO> getAllPayments();

    // Tạo thanh toán thuê phim lẻ
    Payment createMovieRentalPayment(User user, Movie movie);

    // Tìm payment by ID
    java.util.Optional<Payment> findByPaymentId(Long paymentId);
}
