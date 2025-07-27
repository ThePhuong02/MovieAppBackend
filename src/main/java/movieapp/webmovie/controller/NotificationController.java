package movieapp.webmovie.controller;

import movieapp.webmovie.dto.NotificationRequest;
import movieapp.webmovie.entity.Notification;
import movieapp.webmovie.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @PostMapping
    public Notification send(@RequestBody NotificationRequest req) {
        return service.send(req);
    }

    @GetMapping("/{userId}")
    public List<Notification> getByUser(@PathVariable Long userId) {
        return service.getUserNotifications(userId);
    }

    @PutMapping("/{id}/read")
    public void markRead(@PathVariable Long id) {
        service.markAsRead(id);
    }
}
