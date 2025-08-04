package movieapp.webmovie.controller;

import movieapp.webmovie.dto.*;
import movieapp.webmovie.entity.User;
import movieapp.webmovie.enums.Role;
import movieapp.webmovie.security.CustomUserDetails;
import movieapp.webmovie.security.JwtTokenUtil;
import movieapp.webmovie.service.EmailService;
import movieapp.webmovie.service.UserService;
import movieapp.webmovie.service.UserDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
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
    @Autowired
    private UserDeviceService userDeviceService;

    // ✅ Đăng ký
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO dto) {
        if (userService.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email đã tồn tại");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);
        user.setAvatar("/uploads/default-avatar.png"); // Avatar mặc định

        userService.saveUser(user);
        return ResponseEntity.ok("Đăng ký thành công");
    }

    // ✅ Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto, HttpServletRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User user = userDetails.getUser();
        String token = jwtTokenUtil.generateToken(user);

        // Lưu thông tin thiết bị
        String ipAddress = request.getRemoteAddr();
        String deviceName = request.getHeader("User-Agent");
        userDeviceService.saveLoginSession(user, token, ipAddress, deviceName);

        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    // ✅ Lấy thông tin user đang đăng nhập
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User user = userDetails.getUser();

        // Ghép host vào avatar nếu cần
        String avatarUrl = user.getAvatar();
        if (avatarUrl != null && !avatarUrl.startsWith("http")) {
            avatarUrl = "http://localhost:8080" + avatarUrl;
        }

        Map<String, Object> response = Map.of(
                "userID", user.getUserID(),
                "name", user.getName(),
                "email", user.getEmail(),
                "phone", user.getPhone(),
                "avatar", avatarUrl,
                "role", user.getRole());

        return ResponseEntity.ok(response);
    }

    // ✅ Cập nhật thông tin cá nhân
    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileDTO dto, Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User user = userDetails.getUser();
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());

        if (dto.getAvatar() != null && !dto.getAvatar().isBlank()) {
            user.setAvatar(dto.getAvatar());
        }

        userService.saveUser(user);
        return ResponseEntity.ok("Cập nhật thông tin thành công");
    }

    // ✅ Cập nhật avatar qua link
    @PutMapping("/update-avatar-link")
    public ResponseEntity<?> updateAvatarLink(@RequestBody Map<String, String> body, Authentication auth) {
        String avatarUrl = body.get("avatar");
        if (avatarUrl == null || avatarUrl.isBlank()) {
            return ResponseEntity.badRequest().body("Avatar URL không hợp lệ");
        }

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        userService.updateAvatarByLink(userDetails.getUser(), avatarUrl);

        return ResponseEntity.ok(Map.of("avatar", avatarUrl));
    }

    // ✅ Upload avatar file
    @PutMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("avatar") MultipartFile avatarFile, Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User user = userDetails.getUser();
        try {
            userService.updateAvatarByFile(user, avatarFile);

            // Trả về full URL cho frontend
            String fullUrl = "http://localhost:8080" + user.getAvatar();
            return ResponseEntity.ok(Map.of("avatar", fullUrl));

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Lỗi khi upload ảnh");
        }
    }

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

    // ✅ Quên mật khẩu
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

    // ✅ Reset mật khẩu
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

    // ✅ Đăng xuất
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token không hợp lệ");
        }
        String token = authHeader.replace("Bearer ", "");
        userDeviceService.revokeToken(token);
        return ResponseEntity.ok("Đăng xuất thành công");
    }
}
