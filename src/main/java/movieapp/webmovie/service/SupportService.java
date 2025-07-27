package movieapp.webmovie.service;

import movieapp.webmovie.dto.SupportReplyRequest;
import movieapp.webmovie.dto.SupportRequest;
import movieapp.webmovie.dto.SupportResponse;

import java.util.List;

public interface SupportService {
    void sendSupport(SupportRequest request);

    void replySupport(SupportReplyRequest request);

    List<SupportResponse> getAllSupports();

    List<SupportResponse> getSupportsByUserId(Long userId);
}
