package movieapp.webmovie.service;

import movieapp.webmovie.entity.*;

import java.util.List;

public interface PlaybackService {
    void grantPlaybackRight(User user, Movie movie, Payment payment, boolean canDownload, int validDays);

    boolean hasAccessToMovie(User user, Movie movie);

    List<Movie> getAccessibleMovies(User user);

    boolean canDownloadMovie(User user, Movie movie);

    void cancelMovieRental(User user, Long movieId);

    boolean canCancelRental(User user, Long movieId); // <-- Thêm mới
}