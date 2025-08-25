package movieapp.webmovie.controller;

import movieapp.webmovie.dto.PricingPlansResponse;
import movieapp.webmovie.service.PlanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

        @Autowired
        private PlanService planService;

        @GetMapping("/pricing")
        public ResponseEntity<PricingPlansResponse> getPricingPlans() {
                // Lấy data từ database thông qua PlanService
                PricingPlansResponse response = planService.getPricingPlans();
                return ResponseEntity.ok(response);
        }
}
