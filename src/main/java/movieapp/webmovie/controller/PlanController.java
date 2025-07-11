package movieapp.webmovie.controller;

import movieapp.webmovie.dto.PlanRequest;
import movieapp.webmovie.entity.Plan;
import movieapp.webmovie.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
