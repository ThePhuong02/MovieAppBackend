package movieapp.webmovie.service.impl;

import movieapp.webmovie.dto.SubscriptionRequest;
import movieapp.webmovie.entity.Payment;
import movieapp.webmovie.entity.Subscription;
import movieapp.webmovie.repository.SubscriptionRepository;
import movieapp.webmovie.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepo;

    @Override
    public String subscribe(SubscriptionRequest request) {
        return "Chức năng này chưa hỗ trợ.";
    }

    @Override
    public Map<String, Object> getCurrentSubscription(Long userId) {
        Optional<Subscription> optionalSub = subscriptionRepo.findTopByUserIdAndIsActiveTrueOrderByEndDateDesc(userId);

        if (optionalSub.isPresent()) {
            Subscription sub = optionalSub.get();
            long daysLeft = ChronoUnit.DAYS.between(LocalDateTime.now(), sub.getEndDate());
            daysLeft = Math.max(0, daysLeft);

            Map<String, Object> result = new HashMap<>();
            result.put("planId", sub.getPlanId());
            result.put("pricingId", sub.getPricingId());
            result.put("startDate", sub.getStartDate());
            result.put("endDate", sub.getEndDate());
            result.put("daysLeft", daysLeft);
            result.put("isActive", sub.getIsActive());
            result.put("isCancelled", sub.getIsCancelled());
            result.put("autoRenew", sub.getAutoRenew());

            return result;
        }

        throw new RuntimeException("Không tìm thấy gói đăng ký đang hoạt động.");
    }

    @Override
    public Subscription createSubscriptionFromPayment(Payment payment) {
        Long userId = payment.getUserId();
        String pricingId = payment.getPricingId();

        System.out.println("🔍 DEBUG createSubscriptionFromPayment:");
        System.out.println("  - userId: " + userId);
        System.out.println("  - pricingId: " + pricingId);
        System.out.println("  - planId: " + payment.getPlanId());

        // Xác định thời gian dựa trên pricingId
        LocalDateTime now = LocalDateTime.now();
        int duration = getDurationFromPricingId(pricingId);

        Subscription sub = new Subscription();
        sub.setUserId(userId);
        sub.setPlanId(getDefaultPlanId(payment.getPlanId(), pricingId)); // Tạo default planId
        sub.setPricingId(pricingId); // Quan trọng: lưu pricingId
        sub.setStartDate(now);
        sub.setEndDate(now.plusDays(duration));
        sub.setIsActive(true);
        sub.setAutoRenew(true);
        sub.setIsCancelled(false);
        sub.setPaymentId(payment.getPaymentId());

        System.out.println("  - duration: " + duration + " days");
        System.out.println("  - endDate: " + sub.getEndDate());

        Subscription savedSub = subscriptionRepo.save(sub);
        System.out.println("✅ DEBUG: Subscription created with ID: " + savedSub.getSubscriptionId());

        return savedSub;
    }

    private int getDurationFromPricingId(String pricingId) {
        if (pricingId == null)
            return 30;

        if (pricingId.contains("yearly")) {
            return 365; // 1 năm
        } else if (pricingId.contains("monthly")) {
            return 30; // 1 tháng
        }
        return 30; // Default
    }

    @Override
    public boolean hasPremiumAccess(Long userId) {
        Optional<Subscription> optionalSub = subscriptionRepo.findTopByUserIdAndIsActiveTrueOrderByEndDateDesc(userId);

        if (optionalSub.isEmpty()) {
            System.out.println("🔴 DEBUG: No active subscription found for userId: " + userId);
            return false; // Không có subscription active
        }

        Subscription sub = optionalSub.get();
        String pricingId = sub.getPricingId();

        System.out.println("🔍 DEBUG hasPremiumAccess:");
        System.out.println("  - userId: " + userId);
        System.out.println("  - subscriptionId: " + sub.getSubscriptionId());
        System.out.println("  - pricingId: " + pricingId);
        System.out.println("  - planId: " + sub.getPlanId());
        System.out.println("  - isActive: " + sub.getIsActive());
        System.out.println("  - endDate: " + sub.getEndDate());

        // Kiểm tra xem pricingId có grant premium access không
        boolean isPremium = isPremiumPlan(pricingId);
        System.out.println("  - isPremiumPlan result: " + isPremium);

        return isPremium;
    }

    private boolean isPremiumPlan(String pricingId) {
        if (pricingId == null) {
            System.out.println("🔴 DEBUG isPremiumPlan: pricingId is null");
            return false;
        }

        System.out.println("🔍 DEBUG isPremiumPlan: checking pricingId = " + pricingId);

        // Chỉ premium và standard plan mới có quyền truy cập premium content
        boolean result = pricingId.startsWith("premium-") || pricingId.startsWith("standard-");
        System.out.println("🔍 DEBUG isPremiumPlan: result = " + result);

        return result;
    }

    private Long getDefaultPlanId(Long existingPlanId, String pricingId) {
        if (existingPlanId != null) {
            return existingPlanId;
        }

        // Tạo planId mặc định dựa trên pricingId
        if (pricingId == null) {
            return 1L; // Default free plan
        }

        if (pricingId.startsWith("free")) {
            return 1L; // Free plan ID
        } else if (pricingId.startsWith("standard")) {
            return 2L; // Standard plan ID
        } else if (pricingId.startsWith("premium")) {
            return 3L; // Premium plan ID
        }

        return 1L; // Default fallback
    }

    @Override
    public boolean cancelSubscription(Long userId) {
        Optional<Subscription> opt = subscriptionRepo.findTopByUserIdAndIsActiveTrueOrderByEndDateDesc(userId);

        if (opt.isPresent()) {
            Subscription sub = opt.get();
            sub.setIsCancelled(true);
            sub.setAutoRenew(false);
            subscriptionRepo.save(sub);
            return true;
        }

        return false;
    }

    @Override
    public boolean cancelAutoRenew(Long userId) {
        Optional<Subscription> opt = subscriptionRepo.findTopByUserIdAndIsActiveTrueOrderByEndDateDesc(userId);

        if (opt.isPresent()) {
            Subscription sub = opt.get();
            sub.setAutoRenew(false);
            sub.setIsCancelled(true);
            subscriptionRepo.save(sub);
            return true;
        }

        return false;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateExpiredSubscriptions() {
        List<Subscription> activeSubs = subscriptionRepo.findAllByIsActiveTrue();
        for (Subscription sub : activeSubs) {
            if (sub.getEndDate().isBefore(LocalDateTime.now())) {
                sub.setIsActive(false);
                subscriptionRepo.save(sub);
            }
        }
    }
}