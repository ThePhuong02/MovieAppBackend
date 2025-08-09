package movieapp.webmovie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingPlansResponse {
    private List<PricingPlanDTO> monthly;
    private List<PricingPlanDTO> yearly;
}
