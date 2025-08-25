package movieapp.webmovie.service.impl;

import movieapp.webmovie.dto.PricingPlanDTO;
import movieapp.webmovie.dto.PricingPlansResponse;
import movieapp.webmovie.entity.Plan;
import movieapp.webmovie.repository.PlanRepository;
import movieapp.webmovie.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlanServiceImpl implements PlanService {

    @Autowired
    private PlanRepository planRepository;

    @PostConstruct
    public void init() {
        initializeDefaultPlans();
    }

    @Override
    public void initializeDefaultPlans() {
        // Kiểm tra xem đã có plans trong database chưa
        if (planRepository.count() > 0) {
            System.out.println("✅ Plans already exist in database. Skipping initialization.");
            return;
        }

        System.out.println("🔄 Initializing default plans...");

        // Tạo Free Plan
        Plan freePlan = new Plan();
        freePlan.setName("Free Plan");
        freePlan.setPrice(BigDecimal.ZERO);
        freePlan.setDurationDays(365); // Free plan không hết hạn, để 365 days
        freePlan.setDescription(
                "Enjoy an extensive library of movies and shows, featuring a range of content, including recently released titles.");
        freePlan.setGrantsPremiumAccess(false);
        planRepository.save(freePlan);

        // Tạo Standard Plan Monthly
        Plan standardMonthly = new Plan();
        standardMonthly.setName("Standard Plan");
        standardMonthly.setPrice(new BigDecimal("10"));
        standardMonthly.setDurationDays(30);
        standardMonthly.setDescription(
                "Access to a wider selection of movies and shows, including most new releases and exclusive content");
        standardMonthly.setGrantsPremiumAccess(true);
        planRepository.save(standardMonthly);

        // Tạo Standard Plan Yearly
        Plan standardYearly = new Plan();
        standardYearly.setName("Standard Plan");
        standardYearly.setPrice(new BigDecimal("96"));
        standardYearly.setDurationDays(365);
        standardYearly.setDescription(
                "Access to a wider selection of movies and shows, including most new releases and exclusive content");
        standardYearly.setGrantsPremiumAccess(true);
        planRepository.save(standardYearly);

        // Tạo Premium Plan Monthly
        Plan premiumMonthly = new Plan();
        premiumMonthly.setName("Premium Plan");
        premiumMonthly.setPrice(new BigDecimal("20"));
        premiumMonthly.setDurationDays(30);
        premiumMonthly.setDescription(
                "Access to a widest selection of movies and shows, including all new releases and Offline Viewing");
        premiumMonthly.setGrantsPremiumAccess(true);
        planRepository.save(premiumMonthly);

        // Tạo Premium Plan Yearly
        Plan premiumYearly = new Plan();
        premiumYearly.setName("Premium Plan");
        premiumYearly.setPrice(new BigDecimal("192"));
        premiumYearly.setDurationDays(365);
        premiumYearly.setDescription(
                "Access to a widest selection of movies and shows, including all new releases and Offline Viewing");
        premiumYearly.setGrantsPremiumAccess(true);
        planRepository.save(premiumYearly);

        System.out.println("✅ Default plans initialized successfully!");
    }

    @Override
    public PricingPlansResponse getPricingPlans() {
        List<Plan> allPlans = planRepository.findAll();

        // Convert plans từ database thành PricingPlanDTO
        List<PricingPlanDTO> monthly = allPlans.stream()
                .filter(plan ->
                // Free Plan xuất hiện ở cả monthly và yearly
                plan.getName().toLowerCase().contains("free") ||
                // Các plans khác chỉ lấy monthly (30 days)
                        plan.getDurationDays() <= 30)
                .map(plan -> convertToPricingPlanDTO(plan, false)) // false = monthly
                .collect(Collectors.toList());

        List<PricingPlanDTO> yearly = allPlans.stream()
                .filter(plan ->
                // Free Plan xuất hiện ở cả monthly và yearly
                plan.getName().toLowerCase().contains("free") ||
                // Các plans khác chỉ lấy yearly (365 days)
                        plan.getDurationDays() > 30)
                .map(plan -> convertToPricingPlanDTO(plan, true)) // true = yearly
                .collect(Collectors.toList());

        return PricingPlansResponse.builder()
                .monthly(monthly)
                .yearly(yearly)
                .build();
    }

    private PricingPlanDTO convertToPricingPlanDTO(Plan plan, boolean isYearly) {
        String planId = generatePricingId(plan, isYearly);
        String price = plan.getPrice().compareTo(BigDecimal.ZERO) == 0 ? "Free" : "$" + plan.getPrice().toPlainString();
        String period = isYearly ? "/year" : null;

        return PricingPlanDTO.builder()
                .id(planId)
                .title(plan.getName())
                .description(plan.getDescription())
                .price(price)
                .period(period)
                .comingSoon(isComingSoon(plan))
                .build();
    }

    private String generatePricingId(Plan plan, boolean isYearly) {
        String baseName = "";
        if (plan.getName().toLowerCase().contains("free")) {
            baseName = "free";
        } else if (plan.getName().toLowerCase().contains("standard")) {
            baseName = "standard";
        } else if (plan.getName().toLowerCase().contains("premium")) {
            baseName = "premium";
        }

        if (isYearly) {
            return baseName + "-yearly";
        } else {
            return baseName + (baseName.equals("free") ? "" : "-monthly");
        }
    }

    private boolean isComingSoon(Plan plan) {
        // Standard plans are coming soon
        return plan.getName().toLowerCase().contains("standard");
    }

    @Override
    public Optional<Plan> findPlanByNameAndDuration(String name, Integer durationDays) {
        return planRepository.findByNameAndDurationDays(name, durationDays);
    }

    @Override
    public Optional<Plan> findPlanByName(String name) {
        return planRepository.findByName(name);
    }

    @Override
    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    @Override
    public Plan savePlan(Plan plan) {
        return planRepository.save(plan);
    }

    @Override
    public Optional<Plan> findById(Long planId) {
        return planRepository.findById(planId);
    }
}
