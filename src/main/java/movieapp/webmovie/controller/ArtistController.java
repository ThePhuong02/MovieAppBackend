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

import movieapp.webmovie.entity.Artist;
import movieapp.webmovie.service.ArtistService;
@RestController
@RequestMapping("/api/artists")
@PreAuthorize("hasRole('ADMIN')")
public class ArtistController {
      @Autowired
    private ArtistService service;

    @GetMapping
    public List<Artist> all() { return service.getAll(); }

    @GetMapping("/{id}")
    public Artist one(@PathVariable Integer id) { return service.getById(id); }

    @PostMapping
    public Artist create(@RequestBody Artist a) { return service.create(a); }

    @PutMapping("/{id}")
    public Artist update(@PathVariable Integer id, @RequestBody Artist a) {
        return service.update(id, a);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) { service.delete(id); }
}
