package movieapp.webmovie.service;

import movieapp.webmovie.dto.ReportDTO;
import movieapp.webmovie.entity.Report;
import movieapp.webmovie.entity.User;
import movieapp.webmovie.entity.Movie;
import movieapp.webmovie.repository.ReportRepository;
import movieapp.webmovie.repository.UserRepository;
import movieapp.webmovie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Optional<Report> getReportById(Integer id) {
        return reportRepository.findById(id);
    }

    public Report createReport(ReportDTO dto) {
        Report report = new Report();
        report.setContent(dto.getContent());
        report.setStatus(dto.getStatus());

        User user = userRepository.findById(dto.getUserID())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserID()));
        report.setUser(user);

        Movie movie = movieRepository.findById(dto.getMovieID())
                .orElseThrow(() -> new RuntimeException("Movie not found with ID: " + dto.getMovieID()));
        report.setMovie(movie);

        User staff = userRepository.findById(dto.getStaffID())
                .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + dto.getStaffID()));
        // Optional: Kiểm tra role staff nếu muốn
        report.setStaff(staff);

        return reportRepository.save(report);
    }

    public void deleteReport(Integer id) {
        reportRepository.deleteById(id);
    }
}
