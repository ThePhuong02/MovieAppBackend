package movieapp.webmovie.service.impl;

import lombok.RequiredArgsConstructor;
import movieapp.webmovie.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("thandongdatviet357@gmail.com"); // 👈 nên đồng bộ với spring.mail.username
        message.setTo(to);
        message.setSubject("Mã OTP xác thực");
        message.setText("Mã OTP của bạn là: " + otp);
        mailSender.send(message);
    }

    @Override
    public void sendSupportReplyEmail(String to, String userName, String replyContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("thandongdatviet357@gmail.com");
        message.setTo(to);
        message.setSubject("Phản hồi yêu cầu hỗ trợ");
        message.setText(
                "Xin chào " + userName + ",\n\n"
                        + "Yêu cầu hỗ trợ của bạn đã được phản hồi:\n"
                        + replyContent + "\n\n"
                        + "Trân trọng,\nĐội ngũ hỗ trợ StreamVibe");
        mailSender.send(message);
    }

    @Override
    public void sendPaymentSuccessEmail(String to, String planName, BigDecimal amount) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("thandongdatviet357@gmail.com");
        message.setTo(to);
        message.setSubject("Thanh toán thành công - MovieApp 🎉");
        message.setText(
                "Xin chào,\n\n"
                        + "Cảm ơn bạn đã thanh toán thành công gói: " + planName + ".\n"
                        + "Số tiền: " + amount + " USD.\n\n"
                        + "Gói dịch vụ đã được kích hoạt, chúc bạn xem phim vui vẻ!\n\n"
                        + "Trân trọng,\nĐội ngũ MovieApp");
        mailSender.send(message);
    }
}
