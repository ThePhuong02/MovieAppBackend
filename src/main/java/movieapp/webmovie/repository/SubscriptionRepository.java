package movieapp.webmovie.repository;

import movieapp.webmovie.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findTopByUserIdAndIsActiveTrueOrderByEndDateDesc(Integer userId);
}
