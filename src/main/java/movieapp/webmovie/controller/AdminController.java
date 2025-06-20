package movieapp.webmovie.controller;

import movieapp.webmovie.dto.UserResponseDTO;
import movieapp.webmovie.dto.UserRoleUpdateRequest;
import movieapp.webmovie.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // Chỉ ADMIN mới được truy cập
public class AdminController {

    private final UserService userService;

    // ✅ Cập nhật vai trò người dùng
    @PutMapping("/users/{id}/role")
    public ResponseEntity<String> updateUserRole(
            @PathVariable Integer id,
            @Valid @RequestBody UserRoleUpdateRequest request) {

        userService.updateUserRole(id, request.getRole());
        return ResponseEntity.ok("Cập nhật quyền người dùng thành công!");
    }

    // ✅ Lấy toàn bộ danh sách người dùng (ẩn mật khẩu & resetToken)
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUserResponses());
    }
}
