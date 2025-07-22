package movieapp.webmovie.controller;

import movieapp.webmovie.entity.*;
import movieapp.webmovie.dto.*;
import movieapp.webmovie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private ReportService service;

    @PostMapping
    public Report report(@RequestBody ReportRequest request) {
        return service.reportMovie(request);
    }

    @GetMapping
    public List<Report> getAllReports() {
        return service.getAllReports();
    }

    @PutMapping("/{id}")
    public Report update(@PathVariable Long id, @RequestBody ReportUpdateRequest request) {
        return service.updateReport(id, request);
    }
}