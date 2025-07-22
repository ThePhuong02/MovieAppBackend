package movieapp.webmovie.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import movieapp.webmovie.entity.Artist;
import movieapp.webmovie.service.ArtistService;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    @Autowired
    private ArtistService service;

    // ✅ Cho USER và ADMIN xem danh sách nghệ sĩ
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Artist> all() {
        return service.getAll();
    }

    // ✅ Cho USER và ADMIN xem chi tiết nghệ sĩ
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Artist one(@PathVariable Long id) {
        return service.getById(id);
    }

    // ❌ Chỉ ADMIN được tạo nghệ sĩ
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Artist create(@RequestBody Artist a) {
        return service.create(a);
    }

    // ❌ Chỉ ADMIN được cập nhật nghệ sĩ
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Artist update(@PathVariable Long id, @RequestBody Artist a) {
        return service.update(id, a);
    }

    // ❌ Chỉ ADMIN được xóa nghệ sĩ
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
