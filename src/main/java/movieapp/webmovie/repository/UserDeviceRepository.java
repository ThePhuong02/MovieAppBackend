package movieapp.webmovie.repository;

import movieapp.webmovie.entity.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {
    Optional<UserDevice> findByLoginToken(String token);
}