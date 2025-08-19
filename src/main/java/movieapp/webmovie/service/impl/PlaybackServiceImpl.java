package movieapp.webmovie.service.impl;

import movieapp.webmovie.entity.*;
import movieapp.webmovie.enums.AccessLevel;
import movieapp.webmovie.repository.*;
import movieapp.webmovie.service.PlaybackService;
import movieapp.webmovie.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlaybackServiceImpl implements PlaybackService {

    @Autowired
    private PlaybackRightRepository playbackRepo;

    @Autowired
    private MovieRepository movieRepo;

    @Autowired
    private WatchHistoryRepository watchHistoryRepo;

    @Autowired
    private SubscriptionService subscriptionService;

    @Override
    public void grantPlaybackRight(User user, Movie movie, Payment payment, boolean canDownload, int validDays) {
        PlaybackRight right = PlaybackRight.builder()
                .userId(user.getUserID())
                .movieId(movie.getMovieID())
                .paymentId(payment.getPaymentId())
                .canDownload(canDownload)
                .purchasedAt(LocalDateTime.now())
                .expireAt(LocalDateTime.now().plusDays(validDays))
                .build();
        playbackRepo.save(right);
    }

    @Override
    public boolean hasAccessToMovie(User user, Movie movie) {
        if (movie.getAccessLevel() == AccessLevel.FREE)
            return true;

        // Kiểm tra premium access dựa trên pricingId trong subscription
        boolean hasPremiumAccess = subscriptionService.hasPremiumAccess(user.getUserID());
        if (movie.getAccessLevel() == AccessLevel.PREMIUM && hasPremiumAccess)
            return true;

        return playbackRepo.existsByUserIdAndMovieIdAndExpireAtAfter(
                user.getUserID(), movie.getMovieID(), LocalDateTime.now());
    }

    @Override
    public List<Movie> getAccessibleMovies(User user) {
        List<PlaybackRight> rights = playbackRepo
                .findByUserIdAndExpireAtAfter(user.getUserID(), LocalDateTime.now());
        List<Long> movieIds = rights.stream().map(PlaybackRight::getMovieId).toList();
        return movieRepo.findAllById(movieIds);
    }

    @Override
    public boolean canDownloadMovie(User user, Movie movie) {
        return playbackRepo
                .findByUserIdAndMovieIdAndExpireAtAfter(
                        user.getUserID(), movie.getMovieID(), LocalDateTime.now())
                .map(PlaybackRight::getCanDownload)
                .orElse(false);
    }

    @Override
    public void cancelMovieRental(User user, Long movieId) {
        playbackRepo.findByUserIdAndMovieIdAndExpireAtAfter(
                user.getUserID(), movieId, LocalDateTime.now())
                .ifPresent(playbackRepo::delete);
    }

    @Override
    public boolean canCancelRental(User user, Long movieId) {
        if (!playbackRepo.existsByUserIdAndMovieIdAndExpireAtAfter(user.getUserID(), movieId, LocalDateTime.now())) {
            return false;
        }
        return watchHistoryRepo.findByUserIdAndMovieId(user.getUserID(), movieId)
                .map(h -> h.getWatchedPercent() <= 50.0)
                .orElse(true);
    }
}