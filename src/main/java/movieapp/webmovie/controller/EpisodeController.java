package movieapp.webmovie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import movieapp.webmovie.dto.EpisodeDTO;
import movieapp.webmovie.entity.Episode;
import movieapp.webmovie.service.EpisodeService;

@RestController
@RequestMapping("/api/episodes")
public class EpisodeController {

    @Autowired
    private EpisodeService service;

    @PreAuthorize("hasAnyRole('USER','ADMIN','STAFF')")
    @GetMapping
    public List<Episode> all() {
        return service.getAll();
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN','STAFF')")
    @GetMapping("/{id}")
    public Episode getOne(@PathVariable Long id) {
        return service.getById(id);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN','STAFF')")
    @GetMapping("/series/{seriesId}")
    public List<Episode> getBySeries(@PathVariable Long seriesId) {
        return service.getBySeriesId(seriesId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Episode create(@RequestBody EpisodeDTO dto) {
        return service.create(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Episode update(@PathVariable Long id, @RequestBody EpisodeDTO dto) {
        return service.update(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
