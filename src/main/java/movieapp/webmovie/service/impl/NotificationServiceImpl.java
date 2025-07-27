package movieapp.webmovie.service.impl;

import lombok.RequiredArgsConstructor;
import movieapp.webmovie.dto.NotificationRequest;
import movieapp.webmovie.entity.Notification;
import movieapp.webmovie.repository.NotificationRepository;
import movieapp.webmovie.service.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification send(NotificationRequest req) {
        Notification notification = Notification.builder()
                .userId(req.getUserId())
                .title(req.getTitle())
                .message(req.getMessage())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification n = notificationRepository.findById(notificationId).orElseThrow();
        n.setIsRead(true);
        notificationRepository.save(n);
    }
}
