package movieapp.webmovie.repository;

import movieapp.webmovie.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String resetToken);

    // ✅ THÊM DÒNG NÀY để fix lỗi existsByEmail
    boolean existsByEmail(String email);
}
