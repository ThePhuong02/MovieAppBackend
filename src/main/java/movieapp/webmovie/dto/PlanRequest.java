package movieapp.webmovie.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PlanRequest {
    private String name;
    private BigDecimal price;
    private Integer durationDays;
    private String description;
    private Boolean grantsPremiumAccess;
}
