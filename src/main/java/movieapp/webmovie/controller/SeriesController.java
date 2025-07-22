package movieapp.webmovie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import movieapp.webmovie.dto.SeriesDTO;
import movieapp.webmovie.entity.Series;
import movieapp.webmovie.service.SeriesService;

@RestController
@RequestMapping("/api/series")
@PreAuthorize("hasRole('ADMIN')")
public class SeriesController {

    @Autowired
    private SeriesService service;

    @GetMapping
    public List<Series> all() {
        return service.getAll();
    }

    @PostMapping
    public Series create(@RequestBody SeriesDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public Series update(@PathVariable Long id, @RequestBody SeriesDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
