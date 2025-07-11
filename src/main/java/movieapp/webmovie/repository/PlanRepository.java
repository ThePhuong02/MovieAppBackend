package movieapp.webmovie.repository;

import movieapp.webmovie.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    // có thể mở rộng: List<Plan> findByGrantsPremiumAccessTrue();
}
