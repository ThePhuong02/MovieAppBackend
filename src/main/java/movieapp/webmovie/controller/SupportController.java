package movieapp.webmovie.controller;

import lombok.RequiredArgsConstructor;
import movieapp.webmovie.dto.*;
import movieapp.webmovie.service.SupportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supports")
@RequiredArgsConstructor
public class SupportController {

    private final SupportService supportService;

    @PostMapping("/send")
    public ResponseEntity<?> sendSupport(@RequestBody SupportRequest request) {
        supportService.sendSupport(request);
        return ResponseEntity.ok("Support message sent");
    }

    @PostMapping("/reply")
    public ResponseEntity<?> replySupport(@RequestBody SupportReplyRequest request) {
        supportService.replySupport(request);
        return ResponseEntity.ok("Support message replied");
    }

    @GetMapping
    public ResponseEntity<List<SupportResponse>> getAllSupports() {
        return ResponseEntity.ok(supportService.getAllSupports());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SupportResponse>> getSupportsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(supportService.getSupportsByUserId(userId));
    }
}
