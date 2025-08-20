package movieapp.webmovie.controller;

import movieapp.webmovie.entity.*;
import movieapp.webmovie.dto.*;
import movieapp.webmovie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
    @Autowired
    private RatingService service;

    @PostMapping
    public Rating rate(@RequestBody RatingRequest request) {
        return service.rate(request);
    }

    @GetMapping("/{movieId}")
    public List<Rating> getRatings(@PathVariable Long movieId) {
        return service.getRatings(movieId);
    }
}