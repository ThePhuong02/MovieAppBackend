package movieapp.webmovie.service.impl;

import movieapp.webmovie.entity.*;
import movieapp.webmovie.enums.PaymentStatus;
import movieapp.webmovie.enums.PaymentType;
import movieapp.webmovie.repository.*;
import movieapp.webmovie.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private PlanRepository planRepo;

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private SubscriptionRepository subRepo;

    @Override
    public String subscribe(movieapp.webmovie.dto.SubscriptionRequest request) {
        Plan plan = planRepo.findById(request.getPlanId())
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        Payment payment = new Payment();
        payment.setUserId(request.getUserId());
        payment.setAmount(plan.getPrice());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionRef(UUID.randomUUID().toString());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentType(PaymentType.subscription);
        paymentRepo.save(payment);

        Subscription sub = new Subscription();
        sub.setUserId(request.getUserId());
        sub.setPlanId(plan.getPlanId());
        sub.setStartDate(LocalDateTime.now());
        sub.setEndDate(LocalDateTime.now().plusDays(plan.getDurationDays()));
        sub.setIsActive(true);
        sub.setPaymentId(payment.getPaymentId());
        subRepo.save(sub);

        return "Đăng ký gói thành công. Mã giao dịch: " + payment.getTransactionRef();
    }

    @Override
    public Optional<Subscription> getCurrentSubscription(Integer userId) {
        return subRepo.findTopByUserIdAndIsActiveTrueOrderByEndDateDesc(userId);
    }

    // ✅ Gọi sau khi thanh toán PayPal thành công
    @Override
    public Subscription createSubscriptionFromPayment(Payment payment) {
        // Xác minh payment có đầy đủ thông tin
        if (payment == null || payment.getUserId() == null || payment.getAmount() == null) {
            throw new IllegalArgumentException("Thông tin giao dịch không hợp lệ");
        }

        Plan plan = planRepo.findAll().stream()
                .filter(p -> p.getPrice().compareTo(payment.getAmount()) == 0)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy gói phù hợp với số tiền đã thanh toán"));

        Subscription subscription = new Subscription();
        subscription.setUserId(payment.getUserId());
        subscription.setPlanId(plan.getPlanId());
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(LocalDateTime.now().plusDays(plan.getDurationDays()));
        subscription.setIsActive(true);
        subscription.setPaymentId(payment.getPaymentId());

        return subRepo.save(subscription);
    }
}
