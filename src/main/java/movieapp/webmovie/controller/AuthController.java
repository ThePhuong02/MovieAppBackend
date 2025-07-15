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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
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
        user.setPassword(dto.getPassword());
        user.setRole(Role.USER);
        userService.saveUser(user);

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

    // ✅ Cập nhật thông tin cơ bản (không xử lý ảnh file)
    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileDTO dto, Authentication auth) {
        User user = (User) auth.getPrincipal();
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());

        if (dto.getAvatar() != null) {
            user.setAvatar(dto.getAvatar()); // chỉ dùng nếu FE gửi chuỗi avatar
        }

        userService.saveUser(user);
        return ResponseEntity.ok("Cập nhật thông tin thành công");
    }

    // ✅ Gửi link avatar (Postman)
    @PutMapping("/update-avatar-link")
    public ResponseEntity<?> updateAvatarLink(@RequestBody Map<String, String> body, Authentication auth) {
        User user = (User) auth.getPrincipal();
        String avatarUrl = body.get("avatar");

        if (avatarUrl == null || avatarUrl.isBlank()) {
            return ResponseEntity.badRequest().body("Avatar URL không hợp lệ");
        }

        userService.updateAvatarByLink(user, avatarUrl);
        return ResponseEntity.ok("Cập nhật avatar thành công (dùng link)");
    }

    // ✅ Upload ảnh thực tế từ file (FE dùng multipart/form-data)
    @PutMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("avatar") MultipartFile avatarFile, Authentication auth) {
        User user = (User) auth.getPrincipal();
        try {
            userService.updateAvatarByFile(user, avatarFile);
            return ResponseEntity.ok("Upload ảnh thành công");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Lỗi khi upload ảnh");
        }
    }
}
