package movieapp.webmovie.service;

import movieapp.webmovie.entity.*;
import movieapp.webmovie.repository.*;
import movieapp.webmovie.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepo;

    public Notification send(NotificationRequest req) {
        return notificationRepo.save(Notification.builder()
                .userId(req.getUserId())
                .title(req.getTitle())
                .message(req.getMessage())
                .build());
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public void markAsRead(Long notificationId) {
        Notification n = notificationRepo.findById(notificationId).orElseThrow();
        n.setIsRead(true);
        notificationRepo.save(n);
    }
}