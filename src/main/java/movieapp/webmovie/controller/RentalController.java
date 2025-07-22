package movieapp.webmovie.controller;

import movieapp.webmovie.entity.*;
import movieapp.webmovie.repository.MovieRepository;
import movieapp.webmovie.security.JwtTokenUtil;
import movieapp.webmovie.service.PaymentService;
import movieapp.webmovie.service.PlaybackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private MovieRepository movieRepo;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PlaybackService playbackService;

    @PostMapping("/rent")
    public ResponseEntity<?> rentMovie(@RequestParam Long movieId,
            @RequestParam(defaultValue = "false") boolean canDownload,
            HttpServletRequest request) {
        User user = jwtTokenUtil.getUserFromRequest(request);
        Movie movie = movieRepo.findById(movieId).orElseThrow();

        // Tạo Payment
        Payment payment = paymentService.createMovieRentalPayment(user, movie);

        // Cấp quyền phát lại
        playbackService.grantPlaybackRight(user, movie, payment, canDownload, 3); // 3 ngày

        return ResponseEntity.ok("Thuê phim thành công");
    }

    @GetMapping("/my-rented")
    public ResponseEntity<List<Movie>> getMyRentedMovies(HttpServletRequest request) {
        User user = jwtTokenUtil.getUserFromRequest(request);
        List<Movie> movies = playbackService.getAccessibleMovies(user);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/can-watch/{movieId}")
    public ResponseEntity<?> canWatch(@PathVariable Long movieId, HttpServletRequest request) {
        User user = jwtTokenUtil.getUserFromRequest(request);
        Movie movie = movieRepo.findById(movieId).orElseThrow();

        boolean allowed = playbackService.hasAccessToMovie(user, movie);
        return allowed ? ResponseEntity.ok(movie.getVideoURL())
                : ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền xem phim này.");
    }

    @GetMapping("/download/{movieId}")
    public ResponseEntity<?> downloadMovie(@PathVariable Long movieId, HttpServletRequest request) {
        User user = jwtTokenUtil.getUserFromRequest(request);
        Movie movie = movieRepo.findById(movieId).orElseThrow();

        if (playbackService.canDownloadMovie(user, movie)) {
            return ResponseEntity.ok(movie.getVideoURL());
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không được phép tải phim này.");
    }

    @DeleteMapping("/cancel/{movieId}")
    public ResponseEntity<?> cancelRental(@PathVariable Long movieId, HttpServletRequest request) {
        User user = jwtTokenUtil.getUserFromRequest(request);
        if (!playbackService.canCancelRental(user, movieId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bạn không thể hủy thuê phim này.");
        }
        playbackService.cancelMovieRental(user, movieId);
        return ResponseEntity.ok("Đã hủy thuê phim thành công.");
    }

}
