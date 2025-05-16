package movieapp.webmovie.controller;

import movieapp.webmovie.dto.AuthRequest;
import movieapp.webmovie.dto.AuthResponse;
import movieapp.webmovie.entity.User;
import movieapp.webmovie.enums.Role;
import movieapp.webmovie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new AuthResponse("Email đã được sử dụng", null);
        }

        User newUser = new User();
        newUser.setName(request.getName() != null ? request.getName() : "User " + request.getEmail());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = Role.USER;
        try {
            if (request.getRole() != null) {
                role = Role.valueOf(request.getRole().toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            return new AuthResponse("Role không hợp lệ", null);
        }
        newUser.setRole(role);

        User savedUser = userRepository.save(newUser);
        return new AuthResponse("Đăng ký thành công", savedUser.getUserID());
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse("Sai email hoặc mật khẩu", null);
        }

        return new AuthResponse("Đăng nhập thành công", user.getUserID());
    }
}
