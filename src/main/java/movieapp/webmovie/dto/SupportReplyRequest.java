package movieapp.webmovie.dto;

import lombok.Data;

@Data
public class SupportReplyRequest {
    private Long supportID;
    private String response;
    private Long respondedBy;
}
