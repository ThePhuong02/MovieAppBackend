package movieapp.webmovie.service;

import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PayPalService {

    @Autowired
    private PayPalHttpClient payPalClient;

    @Value("${paypal.return.url}")
    private String returnUrl;

    @Value("${paypal.cancel.url}")
    private String cancelUrl;

    public String createOrder(String amount) throws IOException {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        ApplicationContext context = new ApplicationContext()
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl);

        AmountWithBreakdown amountObj = new AmountWithBreakdown()
                .currencyCode("USD")
                .value(amount);

        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
                .amountWithBreakdown(amountObj);

        orderRequest.applicationContext(context)
                .purchaseUnits(List.of(purchaseUnitRequest));

        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);

        Order order = payPalClient.execute(request).result();

        // Lấy link để redirect khách đến PayPal thanh toán
        for (LinkDescription link : order.links()) {
            if (link.rel().equals("approve")) {
                return link.href(); // URL để redirect
            }
        }

        throw new RuntimeException("Không tìm thấy link xác nhận thanh toán");
    }

    public String captureOrder(String orderId) throws IOException {
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
        request.requestBody(new OrderRequest());

        Order order = payPalClient.execute(request).result();

        if (order.status().equals("COMPLETED")) {
            return "Giao dịch thành công! ID: " + order.id();
        } else {
            return "Giao dịch không thành công! Trạng thái: " + order.status();
        }
    }
}
