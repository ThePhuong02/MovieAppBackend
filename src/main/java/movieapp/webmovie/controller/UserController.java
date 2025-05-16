package movieapp.webmovie.controller;

import movieapp.webmovie.dto.UserDTO;
import movieapp.webmovie.entity.User;
import movieapp.webmovie.enums.Role;
import movieapp.webmovie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    // Get all users
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(convertToDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Create new user
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        // Mặc định role nếu chưa có
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(convertToDTO(savedUser));
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(updatedUser.getName());
                    existingUser.setEmail(updatedUser.getEmail());
                    existingUser.setPassword(updatedUser.getPassword());
                    existingUser.setPhone(updatedUser.getPhone());
                    existingUser.setAvatar(updatedUser.getAvatar());
                    if (updatedUser.getRole() != null) {
                        existingUser.setRole(updatedUser.getRole());
                    }
                    User saved = userRepository.save(existingUser);
                    return ResponseEntity.ok(convertToDTO(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Convert User entity to UserDTO
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .userID(user.getUserID().intValue()) // chuyển Integer -> Integer (có thể giữ Integer nếu bạn sửa DTO)
                .name(user.getName())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .role(user.getRole().name())
                .build();
    }
}
