package movieapp.webmovie.repository;

import movieapp.webmovie.entity.Payment;
import movieapp.webmovie.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserId(Long userId);

    Optional<Payment> findByTransactionRef(String transactionRef);

    // Tính tổng revenue từ các payment đã thành công
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.paymentStatus = :status")
    BigDecimal sumAmountByPaymentStatus(PaymentStatus status);

    // Đếm tổng số payment đã thành công
    long countByPaymentStatus(PaymentStatus status);
}
