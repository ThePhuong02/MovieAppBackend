package movieapp.webmovie.controller;

import movieapp.webmovie.dto.SubscriptionRequest;
import movieapp.webmovie.repository.SubscriptionRepository;
import movieapp.webmovie.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionRepository subscriptionRepo;

    @PostMapping
    public ResponseEntity<String> subscribe(@RequestBody SubscriptionRequest request) {
        return ResponseEntity.ok(subscriptionService.subscribe(request));
    }

    @GetMapping("/status/{userId}")
    public ResponseEntity<?> getStatus(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.getCurrentSubscription(userId));
    }

    @PutMapping("/cancel/{userId}")
    public ResponseEntity<?> cancelSubscription(@PathVariable Long userId) {
        boolean cancelled = subscriptionService.cancelSubscription(userId);
        if (cancelled) {
            return ResponseEntity.ok("✅ Gói đăng ký đã được hủy thành công.");
        } else {
            return ResponseEntity.badRequest().body("⚠ Không tìm thấy gói đang hoạt động để hủy.");
        }
    }

    @PutMapping("/cancel-renew/{userId}")
    public ResponseEntity<?> cancelAutoRenew(@PathVariable Long userId) {
        boolean success = subscriptionService.cancelAutoRenew(userId);
        if (success) {
            return ResponseEntity.ok("✅ Đã tắt tự động gia hạn gói.");
        } else {
            return ResponseEntity.badRequest().body("⚠ Không tìm thấy gói đang hoạt động để tắt gia hạn.");
        }
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<?> getHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionRepo.findAllByUserIdOrderByStartDateDesc(userId));
    }
}