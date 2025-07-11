package movieapp.webmovie.dto;

import lombok.Data;

@Data
public class WebhookRequest {
    private String transactionRef;
    private String status; // "success", "failed"
}
