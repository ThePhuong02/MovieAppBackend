package movieapp.webmovie.repository;

import movieapp.webmovie.entity.Support;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportRepository extends JpaRepository<Support, Long> {
    List<Support> findByUser_UserID(Long userId);
}
