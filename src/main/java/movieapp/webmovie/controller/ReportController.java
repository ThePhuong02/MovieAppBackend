package movieapp.webmovie.controller;

import movieapp.webmovie.dto.ReportDTO;
import movieapp.webmovie.entity.Report;
import movieapp.webmovie.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Integer id) {
        return reportService.getReportById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Report> createReport(@RequestBody ReportDTO reportDTO) {
        Report created = reportService.createReport(reportDTO);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Integer id) {
        if (!reportService.getReportById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
