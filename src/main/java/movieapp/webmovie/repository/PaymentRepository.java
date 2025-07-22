package movieapp.webmovie.repository;

import movieapp.webmovie.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserId(Long userId);

    Optional<Payment> findByTransactionRef(String transactionRef);

}
