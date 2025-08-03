package movieapp.webmovie.service.impl;

import lombok.RequiredArgsConstructor;
import movieapp.webmovie.dto.*;
import movieapp.webmovie.entity.*;
import movieapp.webmovie.repository.*;
import movieapp.webmovie.service.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportServiceImpl implements SupportService {

    private final SupportRepository supportRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final EmailServiceImpl emailService; // Dùng EmailServiceImpl để gửi email reply

    @Override
    public void sendSupport(SupportRequest request) {
        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null)
            throw new RuntimeException("User not found");

        Support support = new Support();
        support.setFirstName(request.getFirstName());
        support.setLastName(request.getLastName());
        support.setEmail(request.getEmail());
        support.setPhoneNumber(request.getPhoneNumber());
        support.setMessage(request.getMessage());
        support.setUser(user);
        supportRepository.save(support);
    }

    @Override
    public void replySupport(SupportReplyRequest request) {
        Support support = supportRepository.findById(request.getSupportID()).orElse(null);
        if (support == null)
            throw new RuntimeException("Support not found");

        User staff = userRepository.findById(request.getRespondedBy()).orElse(null);
        if (staff == null)
            throw new RuntimeException("Staff not found");

        support.setResponse(request.getResponse());
        support.setRespondedBy(staff);
        support.setRespondedAt(LocalDateTime.now());
        supportRepository.save(support);

        // Gửi thông báo trong hệ thống
        notificationService.send(NotificationRequest.builder()
                .userId(support.getUser().getUserID())
                .title("Phản hồi hỗ trợ")
                .message("Yêu cầu hỗ trợ của bạn đã được phản hồi.")
                .build());

        // ✅ Gửi email phản hồi cho user
        emailService.sendSupportReplyEmail(
                support.getEmail(),
                support.getFirstName(),
                request.getResponse());
    }

    @Override
    public List<SupportResponse> getAllSupports() {
        return supportRepository.findAll()
                .stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public List<SupportResponse> getSupportsByUserId(Long userId) {
        return supportRepository.findByUser_UserID(userId)
                .stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    private SupportResponse convertToResponse(Support s) {
        SupportResponse res = new SupportResponse();
        res.setSupportID(s.getSupportID());
        res.setFirstName(s.getFirstName());
        res.setLastName(s.getLastName());
        res.setEmail(s.getEmail());
        res.setPhoneNumber(s.getPhoneNumber());
        res.setMessage(s.getMessage());
        res.setResponse(s.getResponse());
        res.setRespondedByName(s.getRespondedBy() != null ? s.getRespondedBy().getName() : null);
        res.setRespondedAt(s.getRespondedAt());
        res.setCreatedAt(s.getCreatedAt());
        return res;
    }
}
