package movieapp.webmovie.service.impl;

import movieapp.webmovie.dto.SubscriptionRequest;
import movieapp.webmovie.entity.Payment;
import movieapp.webmovie.entity.Subscription;
import movieapp.webmovie.repository.PlanRepository;
import movieapp.webmovie.repository.SubscriptionRepository;
import movieapp.webmovie.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepo;

    @Autowired
    private PlanRepository planRepo;

    @Override
    public String subscribe(SubscriptionRequest request) {
        // Logic nếu không dùng PayPal (có thể dùng sau này)
        return "Chức năng này chưa hỗ trợ.";
    }

    @Override
    public Map<String, Object> getCurrentSubscription(Long userId) {
        Optional<Subscription> optionalSub = subscriptionRepo
                .findTopByUserIdAndIsActiveTrueOrderByEndDateDesc(userId);

        if (optionalSub.isPresent()) {
            Subscription sub = optionalSub.get();

            long daysLeft = ChronoUnit.DAYS.between(LocalDateTime.now(), sub.getEndDate());
            if (daysLeft < 0)
                daysLeft = 0;

            Map<String, Object> result = new HashMap<>();
            result.put("planId", sub.getPlanId());
            result.put("startDate", sub.getStartDate());
            result.put("endDate", sub.getEndDate());
            result.put("daysLeft", daysLeft);
            result.put("isActive", sub.getIsActive());

            return result;
        } else {
            throw new RuntimeException("Không tìm thấy gói đăng ký đang hoạt động.");
        }
    }

    @Override
    public Subscription createSubscriptionFromPayment(Payment payment) {
        Long userId = payment.getUserId();
        Long planId = payment.getPlanId();

        LocalDateTime now = LocalDateTime.now();
        int duration = planRepo.findById(planId)
                .map(p -> p.getDurationDays())
                .orElse(30); // default 30 ngày nếu không tìm thấy gói

        Subscription sub = new Subscription();
        sub.setUserId(userId);
        sub.setPlanId(planId);
        sub.setStartDate(now);
        sub.setEndDate(now.plusDays(duration));
        sub.setIsActive(true);
        sub.setPaymentId(payment.getPaymentId());

        return subscriptionRepo.save(sub);
    }
    // movieapp.webmovie.service.impl.SubscriptionServiceImpl.java

    @Override
    public boolean cancelSubscription(Long userId) {
        Optional<Subscription> currentSubOpt = subscriptionRepo
                .findTopByUserIdAndIsActiveTrueOrderByEndDateDesc(userId);
        if (currentSubOpt.isPresent()) {
            Subscription sub = currentSubOpt.get();
            // ❌ Không hủy ngay, chỉ đánh dấu không gia hạn tiếp
            sub.setIsCancelled(true);
            sub.setAutoRenew(false);
            subscriptionRepo.save(sub);
            return true;
        }
        return false;
    }

    @Override
    public boolean cancelAutoRenew(Long userId) {
        Optional<Subscription> opt = subscriptionRepo
                .findTopByUserIdAndIsActiveTrueOrderByEndDateDesc(userId);

        if (opt.isPresent()) {
            Subscription sub = opt.get();
            sub.setAutoRenew(false); // Tắt gia hạn
            subscriptionRepo.save(sub);
            return true;
        }

        return false;
    }

}
