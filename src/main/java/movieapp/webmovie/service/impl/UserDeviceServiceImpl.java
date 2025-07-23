package movieapp.webmovie.service.impl;

import movieapp.webmovie.entity.User;
import movieapp.webmovie.entity.UserDevice;
import movieapp.webmovie.repository.UserDeviceRepository;
import movieapp.webmovie.service.UserDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDeviceServiceImpl implements UserDeviceService {

    @Autowired
    private UserDeviceRepository userDeviceRepository;

    @Override
    public void saveLoginSession(User user, String token, String ipAddress, String deviceName) {
        UserDevice device = new UserDevice();
        device.setUser(user);
        device.setDeviceName(deviceName);
        device.setIpAddress(ipAddress);
        device.setLoginToken(token);
        userDeviceRepository.save(device);
    }

    @Override
    public void revokeToken(String token) {
        userDeviceRepository.findByLoginToken(token).ifPresent(device -> {
            device.setRevoked(true);
            userDeviceRepository.save(device);
        });
    }
}