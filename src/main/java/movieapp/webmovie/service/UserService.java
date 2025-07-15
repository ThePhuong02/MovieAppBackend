package movieapp.webmovie.service;

import movieapp.webmovie.dto.UserResponseDTO;
import movieapp.webmovie.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    // Đăng ký, lưu user (có mã hóa password)
    User saveUser(User user);

    // Tìm user theo email
    Optional<User> findByEmail(String email);

    // Cập nhật/reset OTP token
    void updateResetToken(String email, String token);

    // Tìm theo reset token (OTP)
    Optional<User> findByResetToken(String token);

    // Trả về toàn bộ user (full entity) – dùng cho admin nội bộ
    List<User> getAllUsers();

    // Trả về danh sách user an toàn (ẩn mật khẩu) – dùng cho FE
    List<UserResponseDTO> getAllUserResponses();

    // Cập nhật role (USER, STAFF, ADMIN)
    void updateUserRole(Integer userId, String newRole);

    // ✅ Cập nhật avatar bằng link (Postman hoặc cập nhật thủ công)
    void updateAvatarByLink(User user, String avatarUrl);

    // ✅ Upload avatar bằng file (từ FE)
    void updateAvatarByFile(User user, MultipartFile avatarFile) throws IOException;
}
