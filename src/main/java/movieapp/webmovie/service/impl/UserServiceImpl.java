package movieapp.webmovie.service.impl;

import movieapp.webmovie.dto.UserResponseDTO;
import movieapp.webmovie.entity.User;
import movieapp.webmovie.enums.Role;
import movieapp.webmovie.repository.UserRepository;
import movieapp.webmovie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Lấy host từ application.properties
    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    private final String uploadDir = "uploads/avatars/";

    @Override
    public User saveUser(User user) {
        if (user.getUserID() == null && userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        if (!user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void updateResetToken(String email, String token) {
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isPresent()) {
            User user = optional.get();
            user.setResetToken(token);
            userRepository.save(user);
        }
    }

    @Override
    public Optional<User> findByResetToken(String token) {
        return userRepository.findByResetToken(token);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<UserResponseDTO> getAllUserResponses() {
        return userRepository.findAll().stream()
                .map(u -> new UserResponseDTO(
                        u.getUserID(),
                        u.getName(),
                        u.getEmail(),
                        u.getPhone(),
                        getFullAvatarUrl(u.getAvatar()),
                        u.getRole().name()))
                .toList();
    }

    @Override
    public void updateUserRole(Long userId, String newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        try {
            Role roleEnum = Role.valueOf(newRole.toUpperCase());
            user.setRole(roleEnum);
            userRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Role không hợp lệ! Chỉ chấp nhận: USER, STAFF, ADMIN.");
        }
    }

    @Override
    public void updateAvatarByLink(User user, String avatarUrl) {
        user.setAvatar(avatarUrl);
        userRepository.save(user);
    }

    @Override
    public void updateAvatarByFile(User user, MultipartFile avatarFile) throws IOException {
        // Xóa ảnh cũ nếu tồn tại
        if (user.getAvatar() != null && user.getAvatar().startsWith("/" + uploadDir)) {
            Path oldPath = Paths.get(user.getAvatar().substring(1));
            try {
                Files.deleteIfExists(oldPath);
            } catch (IOException e) {
                System.err.println("Không thể xóa avatar cũ: " + e.getMessage());
            }
        }

        // Lưu ảnh mới
        String fileName = UUID.randomUUID() + "_" + avatarFile.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(avatarFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Lưu đường dẫn tương đối vào DB
        user.setAvatar("/" + uploadDir + fileName);
        userRepository.save(user);
    }

    // ✅ Bổ sung hàm findById
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Trả về full URL để hiển thị ngoài frontend
    private String getFullAvatarUrl(String avatarPath) {
        if (avatarPath == null || avatarPath.isBlank())
            return null;
        if (avatarPath.startsWith("http"))
            return avatarPath;
        return baseUrl + avatarPath;
    }
}
