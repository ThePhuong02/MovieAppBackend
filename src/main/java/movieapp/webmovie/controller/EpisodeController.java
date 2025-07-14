package movieapp.webmovie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import movieapp.webmovie.entity.Episode;
import movieapp.webmovie.service.EpisodeService;

@RestController
@RequestMapping("/api/episodes")
@PreAuthorize("hasRole('ADMIN')")
public class EpisodeController {
    @Autowired private EpisodeService service;

    @GetMapping public List<Episode> all() {
        return service.getAll();
    }

    @GetMapping("/series/{seriesId}")
    public List<Episode> getBySeries(@PathVariable Integer seriesId) {
        return service.getBySeriesId(seriesId);
    }

    @PostMapping public Episode create(@RequestBody Episode ep) {
        return service.create(ep);
    }

    @PutMapping("/{id}") public Episode update(@PathVariable Integer id, @RequestBody Episode ep) {
        return service.update(id, ep);
    }

    @DeleteMapping("/{id}") public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}