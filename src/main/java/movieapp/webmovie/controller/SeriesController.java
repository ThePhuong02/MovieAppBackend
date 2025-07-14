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

import movieapp.webmovie.entity.Series;
import movieapp.webmovie.service.SeriesService;
@RestController
@RequestMapping("/api/series")
@PreAuthorize("hasRole('ADMIN')")
public class SeriesController {
      @Autowired private SeriesService service;

    @GetMapping public List<Series> all() {
        return service.getAll();
    }

    @PostMapping public Series create(@RequestBody Series s) {
        return service.create(s);
    }

    @PutMapping("/{id}") public Series update(@PathVariable Integer id, @RequestBody Series s) {
        return service.update(id, s);
    }

    @DeleteMapping("/{id}") public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
