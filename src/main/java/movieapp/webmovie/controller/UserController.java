package movieapp.webmovie.controller;

import movieapp.webmovie.entity.User;
import movieapp.webmovie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()") // ✅ Chỉ người đã đăng nhập mới được truy cập
public class UserController {

    private final UserRepository userRepository;

    // ✅ Lấy thông tin người dùng hiện tại
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            @CurrentSecurityContext(expression = "authentication") Authentication authentication) {

        String email = authentication.getName(); // Lấy email từ token đăng nhập
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Không trả về password hoặc resetToken
            User safeUser = new User();
            safeUser.setUserID(user.getUserID());
            safeUser.setName(user.getName());
            safeUser.setEmail(user.getEmail());
            safeUser.setPhone(user.getPhone());
            safeUser.setAvatar(user.getAvatar());
            safeUser.setRole(user.getRole());

            return ResponseEntity.ok(safeUser);
        } else {
            return ResponseEntity.badRequest().body("Không tìm thấy thông tin người dùng");
        }
    }
}
