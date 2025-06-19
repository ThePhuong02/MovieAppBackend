// movieapp.webmovie.service.UserService.java
package movieapp.webmovie.service;

import movieapp.webmovie.entity.User;
import java.util.Optional;
import java.util.List;

public interface UserService {
    User saveUser(User user);

    Optional<User> findByEmail(String email);

    void updateResetToken(String email, String token);

    Optional<User> findByResetToken(String token);

    List<User> getAllUsers();

    // ✅ Thêm phương thức cập nhật role
    void updateUserRole(Integer userId, String newRole);
}
