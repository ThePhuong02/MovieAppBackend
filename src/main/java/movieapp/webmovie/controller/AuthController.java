package movieapp.webmovie.controller;

import movieapp.webmovie.dto.*;
import movieapp.webmovie.entity.User;
import movieapp.webmovie.enums.Role;
import movieapp.webmovie.security.JwtTokenUtil;
import movieapp.webmovie.service.EmailService;
import movieapp.webmovie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin // Mở CORS khi test frontend
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO dto) {
        if (userService.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email đã tồn tại");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // ❗ Không mã hóa ở đây
        user.setRole(Role.USER);
        userService.saveUser(user); // ❗ Service sẽ mã hóa

        return ResponseEntity.ok("Đăng ký thành công");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        User user = (User) auth.getPrincipal();
        String token = jwtTokenUtil.generateToken(user);
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody EmailDTO dto) {
        User user = userService.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));
        String otp = String.format("%06d", new Random().nextInt(999999));
        user.setResetToken(otp);
        userService.saveUser(user);
        emailService.sendOtpEmail(dto.getEmail(), otp);
        return ResponseEntity.ok("OTP đã được gửi qua email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO dto) {
        User user = userService.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không đúng"));
        if (!dto.getOtp().equals(user.getResetToken())) {
            return ResponseEntity.badRequest().body("OTP không hợp lệ");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setResetToken(null);
        userService.saveUser(user);
        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileDTO dto, Authentication auth) {
        User user = (User) auth.getPrincipal();
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());

        if (dto.getAvatar() != null) {
            user.setAvatar(dto.getAvatar());
        }

        userService.saveUser(user);
        return ResponseEntity.ok("Cập nhật thành công");
    }
}