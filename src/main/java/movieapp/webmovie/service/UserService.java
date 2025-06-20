package movieapp.webmovie.service;

import movieapp.webmovie.dto.UserResponseDTO;
import movieapp.webmovie.entity.User;
import java.util.Optional;
import java.util.List;

public interface UserService {
    User saveUser(User user);

    Optional<User> findByEmail(String email);

    void updateResetToken(String email, String token);

    Optional<User> findByResetToken(String token);

    List<User> getAllUsers(); // full entity (admin nội bộ dùng)

    List<UserResponseDTO> getAllUserResponses(); // trả về DTO an toàn

    void updateUserRole(Integer userId, String newRole);
}
