package movieapp.webmovie.repository;

import movieapp.webmovie.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    Optional<Plan> findByNameAndDurationDays(String name, Integer durationDays);

    Optional<Plan> findByName(String name);
}
