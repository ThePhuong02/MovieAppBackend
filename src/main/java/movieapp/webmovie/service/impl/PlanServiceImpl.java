package movieapp.webmovie.service.impl;

import movieapp.webmovie.entity.Plan;
import movieapp.webmovie.repository.PlanRepository;
import movieapp.webmovie.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {

    @Autowired
    private PlanRepository planRepository;

    @Override
    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    @Override
    public Plan savePlan(Plan plan) {
        return planRepository.save(plan);
    }
}
