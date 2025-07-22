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
@PreAuthorize("hasRole('ADMIN')")
public class EpisodeController {

    @Autowired
    private EpisodeService service;

    @GetMapping
    public List<Episode> all() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Episode getOne(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/series/{seriesId}")
    public List<Episode> getBySeries(@PathVariable Long seriesId) {
        return service.getBySeriesId(seriesId);
    }

    @PostMapping
    public Episode create(@RequestBody EpisodeDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public Episode update(@PathVariable Long id, @RequestBody EpisodeDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
