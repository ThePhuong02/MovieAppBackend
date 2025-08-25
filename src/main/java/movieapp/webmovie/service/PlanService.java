package movieapp.webmovie.service;

import movieapp.webmovie.dto.PricingPlansResponse;
import movieapp.webmovie.entity.Plan;

import java.util.List;
import java.util.Optional;

public interface PlanService {

    /**
     * Khởi tạo các plans mặc định nếu chưa có trong database
     */
    void initializeDefaultPlans();

    /**
     * Lấy tất cả plans và trả về response theo format FE
     */
    PricingPlansResponse getPricingPlans();

    /**
     * Tìm plan theo name và duration
     */
    Optional<Plan> findPlanByNameAndDuration(String name, Integer durationDays);

    /**
     * Tìm plan theo name
     */
    Optional<Plan> findPlanByName(String name);

    /**
     * Lấy tất cả plans từ database
     */
    List<Plan> getAllPlans();

    /**
     * Tạo hoặc cập nhật plan
     */
    Plan savePlan(Plan plan);

    /**
     * Tìm plan theo ID
     */
    Optional<Plan> findById(Long planId);
}
