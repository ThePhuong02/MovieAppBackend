package movieapp.webmovie.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PricingPlanDTO {
    private String id;
    private String title;
    private String description;
    private String price;
    private String period; // e.g., "/year". For monthly, can be null or empty
    private Boolean comingSoon; // true only for some plans (e.g., Standard)
}
