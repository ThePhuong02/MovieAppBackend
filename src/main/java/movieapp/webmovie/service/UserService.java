package movieapp.webmovie.service;

import movieapp.webmovie.dto.UserResponseDTO;
import movieapp.webmovie.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);

    Optional<User> findByEmail(String email);

    void updateResetToken(String email, String token);

    Optional<User> findByResetToken(String token);

    List<User> getAllUsers();

    List<UserResponseDTO> getAllUserResponses();

    void updateUserRole(Long userId, String newRole);

    void updateAvatarByLink(User user, String avatarUrl);

    void updateAvatarByFile(User user, MultipartFile avatarFile) throws IOException;
}
