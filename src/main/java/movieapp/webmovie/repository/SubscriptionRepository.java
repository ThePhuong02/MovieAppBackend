package movieapp.webmovie.repository;

import movieapp.webmovie.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    // Lấy subscription mới nhất còn hiệu lực
    Optional<Subscription> findTopByUserIdAndIsActiveTrueOrderByEndDateDesc(Long userId);

    // Kiểm tra user có đang có gói đăng ký hoạt động
    boolean existsByUserIdAndIsActiveTrue(Long userId);

    List<Subscription> findAllByUserIdOrderByStartDateDesc(Long userId);

    List<Subscription> findAllByIsActiveTrue();

}
