package movieapp.webmovie.repository;

import movieapp.webmovie.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByStatus(String status);

    List<Report> findByUserId(Long userId);
}
