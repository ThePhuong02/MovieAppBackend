package movieapp.webmovie.service;

import movieapp.webmovie.dto.NotificationRequest;
import movieapp.webmovie.entity.Notification;

import java.util.List;

public interface NotificationService {
    Notification send(NotificationRequest req);

    List<Notification> getUserNotifications(Long userId);

    void markAsRead(Long notificationId);
}
