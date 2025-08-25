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
    private PaymentRepository paymentRepo;

    @Autowired
    private PlanRepository planRepo;

    @Autowired
    private SubscriptionService subscriptionService;

    // ✅ 1. Tạo đơn hàng PayPal
    @PostMapping("/create-order")
    public String createOrder(@RequestBody SubscriptionRequest request) throws IOException {
        // ✅ Kiểm tra đầu vào (chấp nhận pricingId mới hoặc planId cũ)
        if (request.getUserId() == null || request.getPaymentMethod() == null) {
            return "❌ Thiếu thông tin: userId hoặc paymentMethod.";
        }
        if (request.getPricingId() == null && request.getPlanId() == null) {
            return "❌ Thiếu thông tin: pricingId (ưu tiên) hoặc planId.";
        }

        System.out.println("🔍 Nhận request create-order:");
        System.out.println("pricingId: " + request.getPricingId());
        System.out.println("planId: " + request.getPlanId());
        System.out.println("userId: " + request.getUserId());
        System.out.println("paymentMethod: " + request.getPaymentMethod());

        BigDecimal amount;
        Long resolvedPlanId = null;

        // ✅ Nhánh mới: ưu tiên pricingId từ /api/plans/pricing
        if (request.getPricingId() != null) {
            String pricingId = request.getPricingId();
            switch (pricingId) {
                case "free":
                case "free-yearly":
                    amount = BigDecimal.ZERO;
                    // Gói free: không phân biệt theo tháng/năm, map 1 plan duy nhất nếu có
                    resolvedPlanId = planRepo
                            .findByName("Free Plan")
                            .map(Plan::getPlanId)
                            .orElse(null);
                    break;
                case "standard-monthly":
                case "standard-yearly":
                    return "❌ Gói Standard đang Coming Soon. Vui lòng chọn gói khác.";
                case "premium-monthly":
                    amount = new BigDecimal("20");
                    resolvedPlanId = planRepo
                            .findByNameAndDurationDays("Premium Plan", 30)
                            .map(Plan::getPlanId)
                            .orElse(null);
                    break;
                case "premium-yearly":
                    amount = new BigDecimal("192");
                    resolvedPlanId = planRepo
                            .findByNameAndDurationDays("Premium Plan", 365)
                            .map(Plan::getPlanId)
                            .orElse(null);
                    break;
                default:
                    return "❌ pricingId không hợp lệ.";
            }
        } else {
            // 🔁 Backward compatible: dùng planId cũ
            Plan plan = planRepo.findById(request.getPlanId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy gói với ID: " + request.getPlanId()));
            amount = plan.getPrice();
            resolvedPlanId = plan.getPlanId();
        }

        // 👉 Tạo transactionRef duy nhất
        String transactionRef = UUID.randomUUID().toString();

        // ✅ Tạo đối tượng Payment và lưu
        Payment payment = new Payment();
        payment.setUserId(request.getUserId());
        payment.setAmount(amount);
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionRef(transactionRef);
        payment.setPaymentType(PaymentType.subscription);
        payment.setPaidAt(LocalDateTime.now());
        payment.setPlanId(resolvedPlanId);
        payment.setPricingId(request.getPricingId());

        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            // Gói miễn phí: không cần thanh toán qua PayPal
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            paymentRepo.save(payment);
            return "Gói miễn phí được kích hoạt thành công.";
        } else {
            payment.setPaymentStatus(PaymentStatus.PENDING);
            paymentRepo.save(payment);
            return payPalService.createOrder(amount.toPlainString(), transactionRef);
        }
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

                // ✅ Tạo subscription sau khi thanh toán thành công
                try {
                    if (payment.getPaymentType() == PaymentType.subscription) {
                        Subscription subscription = subscriptionService.createSubscriptionFromPayment(payment);
                        System.out.println("✅ Subscription created successfully: " + subscription.getSubscriptionId());
                        return "Thanh toán & kích hoạt gói thành công! Subscription ID: "
                                + subscription.getSubscriptionId();
                    } else {
                        return "Thanh toán thành công!";
                    }
                } catch (Exception e) {
                    System.err.println("❌ Error creating subscription: " + e.getMessage());
                    e.printStackTrace();
                    return "Thanh toán thành công nhưng có lỗi khi tạo subscription: " + e.getMessage();
                }
            } else {
                return "⚠ Không tìm thấy giao dịch với mã: " + transactionRef;
            }
        }

        return "❌ Giao dịch thất bại: " + result;
    }
}
