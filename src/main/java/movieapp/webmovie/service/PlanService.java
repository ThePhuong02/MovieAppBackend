package movieapp.webmovie.service;

import movieapp.webmovie.entity.Plan;

import java.util.List;

public interface PlanService {
    List<Plan> getAllPlans();

    Plan savePlan(Plan plan);
}
