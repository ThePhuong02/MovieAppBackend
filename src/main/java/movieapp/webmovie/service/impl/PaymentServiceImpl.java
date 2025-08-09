package movieapp.webmovie.service.impl;

import movieapp.webmovie.dto.PaymentHistoryDTO;
import movieapp.webmovie.dto.WebhookRequest;
import movieapp.webmovie.entity.Movie;
import movieapp.webmovie.entity.Payment;
import movieapp.webmovie.entity.User;
import movieapp.webmovie.enums.PaymentStatus;
import movieapp.webmovie.enums.PaymentType;
import movieapp.webmovie.repository.PaymentRepository;
import movieapp.webmovie.service.PaymentService;
import movieapp.webmovie.service.SubscriptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @Override
    @Transactional
    public String handleWebhook(WebhookRequest request) {
        Payment payment = paymentRepository.findByTransactionRef(request.getTransactionRef())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if ("success".equalsIgnoreCase(request.getStatus())) {
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            paymentRepository.save(payment);

            // ✅ Sau khi thanh toán thành công, tự động tạo subscription
            if (payment.getPaymentType() == PaymentType.subscription) {
                subscriptionService.createSubscriptionFromPayment(payment);
            }

        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
        }

        return "Webhook handled.";
    }

    @Override
    public List<PaymentHistoryDTO> getUserPayments(Long userId) {
        return paymentRepository.findByUserId(userId)
                .stream()
                .map(this::toHistoryDTO)
                .toList();
    }

    @Override
    public List<PaymentHistoryDTO> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::toHistoryDTO)
                .toList();
    }

    @Override
    @Transactional
    public Payment createMovieRentalPayment(User user, Movie movie) {
        Payment payment = Payment.builder()
                .userId(user.getUserID())
                .amount(new BigDecimal("1.99"))
                .paymentMethod("VISA")
                .transactionRef(UUID.randomUUID().toString())
                .paymentStatus(PaymentStatus.SUCCESS) // Giả lập
                .paymentType(PaymentType.movie_rental)
                .paidAt(LocalDateTime.now())
                .build();

        return paymentRepository.save(payment);
    }

    public Optional<Payment> findByTransactionRef(String ref) {
        return paymentRepository.findByTransactionRef(ref);
    }

    private PaymentHistoryDTO toHistoryDTO(Payment p) {
        String title = null;
        String period = null;
        if (p.getPricingId() != null) {
            switch (p.getPricingId()) {
                case "free-monthly":
                    title = "Free Plan";
                    break;
                case "free-yearly":
                    title = "Free Plan";
                    period = "/year";
                    break;
                case "standard-monthly":
                    title = "Standard Plan";
                    break;
                case "standard-yearly":
                    title = "Standard Plan";
                    period = "/year";
                    break;
                case "premium-monthly":
                    title = "Premium Plan";
                    break;
                case "premium-yearly":
                    title = "Premium Plan";
                    period = "/year";
                    break;
                default:
                    title = null;
            }
        }

        return PaymentHistoryDTO.builder()
                .paymentId(p.getPaymentId())
                .userId(p.getUserId())
                .amount(p.getAmount())
                .paymentMethod(p.getPaymentMethod())
                .paidAt(p.getPaidAt())
                .transactionRef(p.getTransactionRef())
                .paymentStatus(p.getPaymentStatus())
                .paymentType(p.getPaymentType())
                .planId(p.getPlanId())
                .pricingId(p.getPricingId())
                .title(title)
                .period(period)
                .build();
    }
}
