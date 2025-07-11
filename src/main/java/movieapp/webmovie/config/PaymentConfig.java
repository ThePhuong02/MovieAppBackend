package movieapp.webmovie.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {
    public static final String MOMO_ENDPOINT = "https://test-payment.momo.vn/v2/gateway/api/create";
    public static final String PARTNER_CODE = "your_partner_code";
    public static final String ACCESS_KEY = "your_access_key";
    public static final String SECRET_KEY = "your_secret_key";
    public static final String RETURN_URL = "http://localhost:8080/payment-return";
    public static final String NOTIFY_URL = "http://localhost:8080/api/payments/webhook";
}
