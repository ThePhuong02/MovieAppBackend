package movieapp.webmovie.controller;

import movieapp.webmovie.dto.PlanRequest;
import movieapp.webmovie.dto.PricingPlanDTO;
import movieapp.webmovie.dto.PricingPlansResponse;
import movieapp.webmovie.entity.Plan;
import movieapp.webmovie.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping
    public ResponseEntity<List<Plan>> getAllPlans() {
        return ResponseEntity.ok(planService.getAllPlans());
    }

    @PostMapping
    public ResponseEntity<Plan> createPlan(@RequestBody PlanRequest request) {
        Plan plan = new Plan();
        plan.setName(request.getName());
        plan.setPrice(request.getPrice());
        plan.setDurationDays(request.getDurationDays());
        plan.setDescription(request.getDescription());
        plan.setGrantsPremiumAccess(request.getGrantsPremiumAccess());

        Plan saved = planService.savePlan(plan);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/pricing")
    public ResponseEntity<PricingPlansResponse> getPricingPlans() {
        // Static payload to match FE contract
        List<PricingPlanDTO> monthly = Arrays.asList(
                PricingPlanDTO.builder()
                        .id("free-monthly")
                        .title("Free Plan")
                        .description(
                                "Enjoy an extensive library of movies and shows, featuring a range of content, including recently released titles.")
                        .price("Free")
                        .period(null)
                        .build(),
                PricingPlanDTO.builder()
                        .id("standard-monthly")
                        .title("Standard Plan")
                        .description(
                                "Access to a wider selection of movies and shows, including most new releases and exclusive content")
                        .price("$10")
                        .comingSoon(true)
                        .build(),
                PricingPlanDTO.builder()
                        .id("premium-monthly")
                        .title("Premium Plan")
                        .description(
                                "Access to a widest selection of movies and shows, including all new releases and Offline Viewing")
                        .price("$20")
                        .build());

        List<PricingPlanDTO> yearly = Arrays.asList(
                PricingPlanDTO.builder()
                        .id("free-yearly")
                        .title("Free Plan")
                        .description(
                                "Enjoy an extensive library of movies and shows, featuring a range of content, including recently released titles.")
                        .price("Free")
                        .period(null)
                        .build(),
                PricingPlanDTO.builder()
                        .id("standard-yearly")
                        .title("Standard Plan")
                        .description(
                                "Access to a wider selection of movies and shows, including most new releases and exclusive content")
                        .price("$96")
                        .period("/year")
                        .comingSoon(true)
                        .build(),
                PricingPlanDTO.builder()
                        .id("premium-yearly")
                        .title("Premium Plan")
                        .description(
                                "Access to a widest selection of movies and shows, including all new releases and Offline Viewing")
                        .price("$192")
                        .period("/year")
                        .build());

        PricingPlansResponse response = PricingPlansResponse.builder()
                .monthly(monthly)
                .yearly(yearly)
                .build();
        return ResponseEntity.ok(response);
    }
}
