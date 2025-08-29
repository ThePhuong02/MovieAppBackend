package movieapp.webmovie.service;

import lombok.RequiredArgsConstructor;
import movieapp.webmovie.dto.DashboardStatsDTO;
import movieapp.webmovie.enums.PaymentStatus;
import movieapp.webmovie.repository.PaymentRepository;
import movieapp.webmovie.repository.SubscriptionRepository;
import movieapp.webmovie.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    /**
     * Lấy thống kê tổng quan cho dashboard admin
     * 
     * @return DashboardStatsDTO chứa các thông tin thống kê
     */
    public DashboardStatsDTO getDashboardStats() {
        // Tính tổng revenue từ các payment đã thành công
        BigDecimal totalRevenue = paymentRepository.sumAmountByPaymentStatus(PaymentStatus.SUCCESS);

        // Đếm tổng số subscription
        Long totalSubscriptions = subscriptionRepository.count();

        // Đếm số subscription đang hoạt động
        Long activeSubscriptions = (long) subscriptionRepository.findAllByIsActiveTrue().size();

        // Đếm tổng số user
        Long totalUsers = userRepository.count();

        // Đếm tổng số payment đã thành công
        Long totalPayments = paymentRepository.countByPaymentStatus(PaymentStatus.SUCCESS);

        return DashboardStatsDTO.builder()
                .totalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO)
                .totalSubscriptions(totalSubscriptions)
                .activeSubscriptions(activeSubscriptions)
                .totalUsers(totalUsers)
                .totalPayments(totalPayments)
                .build();
    }
}
