package movieapp.webmovie.controller;

import movieapp.webmovie.dto.SubscriptionRequest;
import movieapp.webmovie.entity.*;
import movieapp.webmovie.enums.PaymentStatus;
import movieapp.webmovie.enums.PaymentType;
import movieapp.webmovie.repository.PaymentRepository;
import movieapp.webmovie.repository.PlanRepository;
import movieapp.webmovie.service.PayPalService;
import movieapp.webmovie.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @Autowired
    private PlanRepository planRepo;

    // ✅ 1. Tạo đơn hàng PayPal
    @PostMapping("/create-order")
    public String createOrder(@RequestBody SubscriptionRequest request) throws IOException {
        // ✅ Kiểm tra null
        if (request.getPlanId() == null || request.getUserId() == null || request.getPaymentMethod() == null) {
            return "❌ Thiếu thông tin: planId, userId hoặc paymentMethod.";
        }

        // 🪵 Log request
        System.out.println("🔍 Nhận request create-order:");
        System.out.println("PlanID: " + request.getPlanId());
        System.out.println("UserID: " + request.getUserId());
        System.out.println("Payment Method: " + request.getPaymentMethod());

        // 🔍 Tìm plan theo ID
        Plan plan = planRepo.findById(request.getPlanId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy gói với ID: " + request.getPlanId()));

        // 👉 Tạo transactionRef duy nhất
        String transactionRef = UUID.randomUUID().toString();

        // ✅ Tạo đối tượng Payment và lưu
        Payment payment = new Payment();
        payment.setUserId(request.getUserId());
        payment.setAmount(plan.getPrice());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionRef(transactionRef);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentType(PaymentType.subscription);
        payment.setPaidAt(LocalDateTime.now());
        payment.setPlanId(request.getPlanId());

        paymentRepo.save(payment);

        // ✅ Gửi đơn hàng đến PayPal
        return payPalService.createOrder(plan.getPrice().toPlainString(), transactionRef);
    }

    // ✅ 2. Capture đơn hàng PayPal sau khi thanh toán thành công
    @GetMapping("/capture-order")
    public String captureOrder(@RequestParam("token") String orderId,
            @RequestParam("transactionRef") String transactionRef) throws IOException {
        // Gửi yêu cầu xác nhận với PayPal
        String result = payPalService.captureOrder(orderId);

        if (result.startsWith("Giao dịch thành công")) {
            // 🔍 Tìm lại payment đã lưu bằng transactionRef
            Optional<Payment> optionalPayment = paymentRepo.findByTransactionRef(transactionRef);

            if (optionalPayment.isPresent()) {
                Payment payment = optionalPayment.get();

                // ✅ Cập nhật trạng thái thành công
                payment.setPaymentStatus(PaymentStatus.SUCCESS);
                paymentRepo.save(payment);

                // ✅ Tạo gói đăng ký từ payment
                Subscription createdSub = subscriptionService.createSubscriptionFromPayment(payment);
                return "✅ Thanh toán & kích hoạt gói thành công. Subscription ID: " + createdSub.getSubscriptionId();
            } else {
                return "⚠ Không tìm thấy giao dịch với mã: " + transactionRef;
            }
        }

        return "❌ Giao dịch thất bại: " + result;
    }
}
