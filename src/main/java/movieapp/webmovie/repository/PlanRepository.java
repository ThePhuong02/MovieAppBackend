package movieapp.webmovie.repository;

import movieapp.webmovie.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    // có thể mở rộng: List<Plan> findByGrantsPremiumAccessTrue();

    Optional<Plan> findByNameAndDurationDays(String name, Integer durationDays);
}
