package movieapp.webmovie.controller;

import movieapp.webmovie.dto.WebhookRequest;
import movieapp.webmovie.entity.Payment;
import movieapp.webmovie.entity.User;
import movieapp.webmovie.service.PaymentService;
import movieapp.webmovie.service.UserService;
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

    // ✅ Webhook từ PayPal hoặc các cổng thanh toán
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody WebhookRequest request) {
        return ResponseEntity.ok(paymentService.handleWebhook(request));
    }

    // ✅ Lấy danh sách thanh toán của người dùng hiện tại (user self-check)
    @GetMapping("/me")
    public ResponseEntity<List<Payment>> getMyPayments(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername(); // lấy email từ token
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        return ResponseEntity.ok(paymentService.getUserPayments(user.getUserID()));
    }

    // ✅ Admin: xem thanh toán của 1 user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getPaymentsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentService.getUserPayments(userId));
    }

    // ✅ Admin: xem toàn bộ thanh toán
    @GetMapping("/all")
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}
