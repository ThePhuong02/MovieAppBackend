package movieapp.webmovie.service.impl;

import movieapp.webmovie.dto.WebhookRequest;
import movieapp.webmovie.entity.Payment;
import movieapp.webmovie.enums.PaymentStatus;
import movieapp.webmovie.repository.PaymentRepository;
import movieapp.webmovie.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public String handleWebhook(WebhookRequest request) {
        Payment payment = paymentRepository.findByTransactionRef(request.getTransactionRef())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if ("success".equalsIgnoreCase(request.getStatus())) {
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
        }

        paymentRepository.save(payment);
        return "Webhook handled.";
    }

    @Override
    public List<Payment> getUserPayments(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
