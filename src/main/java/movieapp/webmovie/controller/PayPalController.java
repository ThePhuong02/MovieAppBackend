package movieapp.webmovie.controller;

import movieapp.webmovie.dto.SubscriptionRequest;
import movieapp.webmovie.entity.Payment;
import movieapp.webmovie.entity.Subscription;
import movieapp.webmovie.enums.PaymentStatus;
import movieapp.webmovie.enums.PaymentType;
import movieapp.webmovie.repository.PaymentRepository;
import movieapp.webmovie.service.PayPalService;
import movieapp.webmovie.service.SubscriptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/paypal")
public class PayPalController {

    @Autowired
    private PayPalService payPalService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private PaymentRepository paymentRepo;

    // ✅ 1. Tạo đơn hàng PayPal → Trả về redirect link
    @PostMapping("/create-order")
    public String createOrder(@RequestBody SubscriptionRequest request) throws IOException {
        // 🔐 Tạo bản ghi Payment trước (trạng thái PENDING)
        Payment payment = new Payment();
        payment.setUserId(request.getUserId());
        payment.setAmount(BigDecimal.valueOf(10.00)); // 👉 Có thể lấy từ Plan
        payment.setPaymentMethod("paypal");
        String transactionRef = UUID.randomUUID().toString();
        payment.setTransactionRef(transactionRef);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentType(PaymentType.subscription);
        payment.setPaidAt(LocalDateTime.now());
        paymentRepo.save(payment);

        // ✅ Tạo đơn hàng PayPal và trả về URL để redirect người dùng
        return payPalService.createOrder(payment.getAmount().toPlainString());
    }

    // ✅ 2. PayPal redirect về sau khi thanh toán → Gọi capture + tạo subscription
    @GetMapping("/capture-order")
    public String captureOrder(@RequestParam("token") String orderId) throws IOException {
        String result = payPalService.captureOrder(orderId);

        if (result.startsWith("Giao dịch thành công")) {
            // 🔍 Tìm payment theo transactionRef nếu có
            Optional<Payment> optionalPayment = paymentRepo.findAll().stream()
                    .filter(p -> p.getPaymentMethod().equals("paypal") &&
                            p.getPaymentStatus() == PaymentStatus.PENDING)
                    .findFirst();

            if (optionalPayment.isPresent()) {
                Payment payment = optionalPayment.get();
                // ✅ Cập nhật trạng thái SUCCESS
                payment.setPaymentStatus(PaymentStatus.SUCCESS);
                paymentRepo.save(payment);

                // ✅ Tạo subscription tương ứng
                Subscription createdSub = subscriptionService.createSubscriptionFromPayment(payment);
                return "✅ Thanh toán & kích hoạt gói thành công. ID: " + createdSub.getSubscriptionId();
            } else {
                return "⚠ Không tìm thấy giao dịch tương ứng!";
            }
        }

        return "❌ Giao dịch thất bại: " + result;
    }
}
