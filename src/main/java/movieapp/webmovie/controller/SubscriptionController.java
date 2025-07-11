package movieapp.webmovie.controller;

import movieapp.webmovie.dto.SubscriptionRequest;
import movieapp.webmovie.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<String> subscribe(@RequestBody SubscriptionRequest request) {
        return ResponseEntity.ok(subscriptionService.subscribe(request));
    }

    @GetMapping("/status/{userId}")
    public ResponseEntity<?> getStatus(@PathVariable Integer userId) {
        return ResponseEntity.ok(subscriptionService.getCurrentSubscription(userId));
    }
}
