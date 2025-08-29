package movieapp.webmovie.service;

import java.math.BigDecimal;

public interface EmailService {
    void sendOtpEmail(String to, String otp);

    void sendSupportReplyEmail(String to, String userName, String replyContent);

    void sendPaymentSuccessEmail(String to, String planName, BigDecimal amount);
}
