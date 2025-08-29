package movieapp.webmovie.controller;

import movieapp.webmovie.dto.SubscriptionRequest;
import movieapp.webmovie.entity.*;
import movieapp.webmovie.enums.PaymentStatus;
import movieapp.webmovie.enums.PaymentType;
import movieapp.webmovie.repository.PaymentRepository;
import movieapp.webmovie.repository.PlanRepository;
import movieapp.webmovie.service.EmailService;
import movieapp.webmovie.service.PayPalService;
import movieapp.webmovie.service.SubscriptionService;
import movieapp.webmovie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    // ✅ 1. Tạo đơn hàng PayPal
    @PostMapping("/create-order")
    public String createOrder(@RequestBody SubscriptionRequest request) throws IOException {
        if (request.getUserId() == null || request.getPaymentMethod() == null) {
            return "❌ Thiếu thông tin: userId hoặc paymentMethod.";
        }
        if (request.getPricingId() == null && request.getPlanId() == null) {
            return "❌ Thiếu thông tin: pricingId (ưu tiên) hoặc planId.";
        }

        BigDecimal amount;
        Long resolvedPlanId = null;

        if (request.getPricingId() != null) {
            switch (request.getPricingId()) {
                case "free":
                case "free-yearly":
                    amount = BigDecimal.ZERO;
                    resolvedPlanId = planRepo.findByName("Free Plan")
                            .map(Plan::getPlanId).orElse(null);
                    break;
                case "standard-monthly":
                case "standard-yearly":
                    return "❌ Gói Standard đang Coming Soon. Vui lòng chọn gói khác.";
                case "premium-monthly":
                    amount = new BigDecimal("20");
                    resolvedPlanId = planRepo.findByNameAndDurationDays("Premium Plan", 30)
                            .map(Plan::getPlanId).orElse(null);
                    break;
                case "premium-yearly":
                    amount = new BigDecimal("192");
                    resolvedPlanId = planRepo.findByNameAndDurationDays("Premium Plan", 365)
                            .map(Plan::getPlanId).orElse(null);
                    break;
                default:
                    return "❌ pricingId không hợp lệ.";
            }
        } else {
            Plan plan = planRepo.findById(request.getPlanId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy gói với ID: " + request.getPlanId()));
            amount = plan.getPrice();
            resolvedPlanId = plan.getPlanId();
        }

        String transactionRef = UUID.randomUUID().toString();

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
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            paymentRepo.save(payment);
            return "Gói miễn phí được kích hoạt thành công.";
        } else {
            payment.setPaymentStatus(PaymentStatus.PENDING);
            paymentRepo.save(payment);
            return payPalService.createOrder(amount.toPlainString(), transactionRef);
        }
    }

    // ✅ 2. Capture đơn hàng PayPal
    @GetMapping("/capture-order")
    public String captureOrder(@RequestParam("token") String orderId,
            @RequestParam("transactionRef") String transactionRef) throws IOException {
        String result = payPalService.captureOrder(orderId);

        if (result.startsWith("Giao dịch thành công")) {
            Optional<Payment> optionalPayment = paymentRepo.findByTransactionRef(transactionRef);

            if (optionalPayment.isPresent()) {
                Payment payment = optionalPayment.get();
                payment.setPaymentStatus(PaymentStatus.SUCCESS);
                paymentRepo.save(payment);

                try {
                    if (payment.getPaymentType() == PaymentType.subscription) {
                        Subscription subscription = subscriptionService.createSubscriptionFromPayment(payment);

                        // ✅ Gửi email xác nhận thanh toán
                        userService.findById(payment.getUserId()).ifPresent(user -> {
                            String email = user.getEmail();
                            Plan plan = planRepo.findById(payment.getPlanId()).orElse(null);
                            if (plan != null) {
                                LocalDate start = subscription.getStartDate().toLocalDate();
                                LocalDate end = subscription.getEndDate().toLocalDate();
                                emailService.sendPaymentSuccessEmail(email, plan.getName(),
                                        payment.getAmount(), start, end);
                            }
                        });

                        return "Thanh toán & kích hoạt gói thành công! Subscription ID: "
                                + subscription.getSubscriptionId();
                    } else {
                        return "Thanh toán thành công!";
                    }
                } catch (Exception e) {
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
