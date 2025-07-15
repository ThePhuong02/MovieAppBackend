package movieapp.webmovie.service.impl;

import movieapp.webmovie.dto.UserResponseDTO;
import movieapp.webmovie.entity.User;
import movieapp.webmovie.enums.Role;
import movieapp.webmovie.repository.UserRepository;
import movieapp.webmovie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final String uploadDir = "uploads/avatars/";

    @Override
    public User saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
                        u.getAvatar(),
                        u.getRole().name()))
                .toList();
    }

    @Override
    public void updateUserRole(Integer userId, String newRole) {
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
        String fileName = UUID.randomUUID() + "_" + avatarFile.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, avatarFile.getBytes());

        user.setAvatar("/" + uploadDir + fileName); // để truy cập qua URL tĩnh
        userRepository.save(user);
    }
}
