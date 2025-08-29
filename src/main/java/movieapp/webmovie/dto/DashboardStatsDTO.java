package movieapp.webmovie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {

    private BigDecimal totalRevenue;
    private Long totalSubscriptions;
    private Long totalUsers;
    private Long activeSubscriptions;
    private Long totalPayments;
}
