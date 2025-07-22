package movieapp.webmovie.service;

import movieapp.webmovie.entity.*;
import movieapp.webmovie.repository.*;
import movieapp.webmovie.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepo;

    public Report reportMovie(ReportRequest req) {
        return reportRepo.save(Report.builder()
                .userId(req.getUserId())
                .movieId(req.getMovieId())
                .content(req.getContent())
                .status("Chưa xử lý")
                .reportedAt(LocalDateTime.now()) // 👈 Bổ sung dòng này
                .build());
    }

    public List<Report> getAllReports() {
        return reportRepo.findAll();
    }

    public Report updateReport(Long reportId, ReportUpdateRequest req) {
        Report r = reportRepo.findById(reportId).orElseThrow();
        r.setStatus(req.getStatus());
        r.setProcessedAt(LocalDateTime.now());
        r.setStaffId(req.getStaffId());
        return reportRepo.save(r);
    }
}
