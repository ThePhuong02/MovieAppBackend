package movieapp.webmovie.service.impl;

import lombok.RequiredArgsConstructor;
import movieapp.webmovie.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendOtpEmail(String to, String otp) {
        // Trường hợp gửi OTP (nếu cần dùng)
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your_email@gmail.com"); // Thay bằng email gửi
        message.setTo(to);
        message.setSubject("Mã OTP xác thực");
        message.setText("Mã OTP của bạn là: " + otp);
        mailSender.send(message);
    }

    // Gửi email phản hồi support
    public void sendSupportReplyEmail(String to, String userName, String replyContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your_email@gmail.com"); // Thay bằng email gửi
        message.setTo(to);
        message.setSubject("Phản hồi yêu cầu hỗ trợ");
        message.setText(
                "Xin chào " + userName + ",\n\n"
                        + "Yêu cầu hỗ trợ của bạn đã được phản hồi:\n"
                        + replyContent + "\n\n"
                        + "Trân trọng,\nĐội ngũ hỗ trợ StreamVibe");
        mailSender.send(message);
    }
}
