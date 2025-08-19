package movieapp.webmovie.controller;

import movieapp.webmovie.dto.PaymentHistoryDTO;
import movieapp.webmovie.dto.WebhookRequest;
import movieapp.webmovie.entity.Payment;
import movieapp.webmovie.entity.Subscription;
import movieapp.webmovie.entity.User;
import movieapp.webmovie.enums.PaymentStatus;
import movieapp.webmovie.enums.PaymentType;
import movieapp.webmovie.service.PaymentService;
import movieapp.webmovie.service.SubscriptionService;
import movieapp.webmovie.service.UserService;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionService subscriptionService;

    // ✅ Webhook từ PayPal hoặc các cổng thanh toán
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody WebhookRequest request) {
        return ResponseEntity.ok(paymentService.handleWebhook(request));
    }

    // ✅ Lấy danh sách thanh toán của người dùng hiện tại (user self-check)
    @GetMapping("/me")
    public ResponseEntity<List<PaymentHistoryDTO>> getMyPayments(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername(); // lấy email từ token
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        return ResponseEntity.ok(paymentService.getUserPayments(user.getUserID()));
    }

    // ✅ Admin: xem thanh toán của 1 user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentHistoryDTO>> getPaymentsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentService.getUserPayments(userId));
    }

    // ✅ Admin: xem toàn bộ thanh toán
    @GetMapping("/all")
    public ResponseEntity<List<PaymentHistoryDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    // 🔧 Debug: Manually tạo subscription từ payment
    @PostMapping("/admin/create-subscription/{paymentId}")
    public ResponseEntity<String> createSubscriptionFromPayment(@PathVariable Long paymentId) {
        try {
            Optional<Payment> paymentOpt = paymentService.findByPaymentId(paymentId);
            if (paymentOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Payment not found");
            }

            Payment payment = paymentOpt.get();
            if (payment.getPaymentType() != PaymentType.subscription) {
                return ResponseEntity.badRequest().body("Payment is not subscription type");
            }

            if (payment.getPaymentStatus() != PaymentStatus.SUCCESS) {
                return ResponseEntity.badRequest().body("Payment is not successful");
            }

            Subscription sub = subscriptionService.createSubscriptionFromPayment(payment);
            return ResponseEntity.ok("✅ Subscription created with ID: " + sub.getSubscriptionId());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Error: " + e.getMessage());
        }
    }
}
