package movieapp.webmovie.service;

import movieapp.webmovie.dto.LoginRequest;
import movieapp.webmovie.dto.RegisterRequest;
import movieapp.webmovie.entity.User;
import movieapp.webmovie.enums.Role;
import movieapp.webmovie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    public String login(LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        Optional<User> userOpt = userRepo.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // So sánh mật khẩu (nên mã hóa, kiểm tra mật khẩu mã hóa)
            if (user.getPassword().equals(password)) {
                // Trả về role dưới dạng String (USER, STAFF, ADMIN)
                return user.getRole().name();
            }
        }

        return null; // đăng nhập thất bại
    }

    public String register(RegisterRequest request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            return "Email already registered!";
        }

        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword()); // Nên mã hóa mật khẩu trước khi lưu
        newUser.setRole(Role.USER); // Mặc định tạo mới luôn là USER
        userRepo.save(newUser);

        return "SUCCESS";
    }
}
