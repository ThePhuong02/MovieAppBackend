package movieapp.webmovie.service;

import movieapp.webmovie.entity.User;

public interface UserDeviceService {
    void saveLoginSession(User user, String token, String ipAddress, String deviceName);

    void revokeToken(String token);
}