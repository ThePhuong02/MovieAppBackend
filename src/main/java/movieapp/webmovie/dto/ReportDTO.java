package movieapp.webmovie.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {
    private Integer reportID;
    private Integer userID; // ID của user báo cáo (hoặc người dùng liên quan)
    private Integer movieID;
    private String content;
    private Integer staffID; // ID của staff (thực ra cũng là user có role = STAFF)
    private String status;
}
