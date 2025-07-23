package movieapp.webmovie.controller;

import movieapp.webmovie.dto.ChangePasswordDTO;
import movieapp.webmovie.entity.User;
import movieapp.webmovie.security.CustomUserDetails;
import movieapp.webmovie.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // ✅ Đổi mật khẩu
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO dto, Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User user = userDetails.getUser();

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Mật khẩu cũ không đúng");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userService.saveUser(user);

        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }
}
