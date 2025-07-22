package movieapp.webmovie.dto;

import lombok.Data;

@Data
public class ReportRequest {
    private Long userId;
    private Long movieId;
    private String content;
}