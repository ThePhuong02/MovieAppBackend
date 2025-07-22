package movieapp.webmovie.dto;

import lombok.Data;

@Data
public class ReportUpdateRequest {
    private Long staffId;
    private String status;
}