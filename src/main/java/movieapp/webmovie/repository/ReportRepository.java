package movieapp.webmovie.repository;

import movieapp.webmovie.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    // Custom query methods (if needed)
}
