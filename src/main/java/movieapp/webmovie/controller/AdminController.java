package movieapp.webmovie.controller;

import movieapp.webmovie.dto.UserRoleUpdateRequest;
import movieapp.webmovie.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // Chỉ cho phép người có ROLE_ADMIN gọi các endpoint trong controller này
public class AdminController {

    private final UserService userService;

    @PutMapping("/users/{id}/role")
    public ResponseEntity<String> updateUserRole(
            @PathVariable Integer id, // ✅ Sửa Long → Integer để khớp với UserServiceImpl
            @Valid @RequestBody UserRoleUpdateRequest request) {

        userService.updateUserRole(id, request.getRole());
        return ResponseEntity.ok("Cập nhật quyền người dùng thành công!");
    }
}
