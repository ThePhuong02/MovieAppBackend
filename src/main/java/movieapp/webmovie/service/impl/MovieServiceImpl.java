package movieapp.webmovie.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import movieapp.webmovie.dto.GenreDTO;
import movieapp.webmovie.dto.MovieDTO;
import movieapp.webmovie.dto.MovieRequestDTO;
import movieapp.webmovie.entity.Genre;
import movieapp.webmovie.entity.Movie;
import movieapp.webmovie.enums.AccessLevel;
import movieapp.webmovie.repository.GenreRepository;
import movieapp.webmovie.repository.MovieRepository;
import movieapp.webmovie.security.CustomUserDetails;
import movieapp.webmovie.service.MovieService;
import movieapp.webmovie.service.SubscriptionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

        private final MovieRepository movieRepository;
        private final GenreRepository genreRepository;
        private final SubscriptionService subscriptionService;

        // ---------- Helpers ----------
        private MovieDTO toDTO(Movie m) {
                return MovieDTO.builder()
                                .movieID(m.getMovieID())
                                .title(m.getTitle())
                                .description(m.getDescription())
                                .duration(m.getDuration())
                                .year(m.getYear())
                                .poster(m.getPoster())
                                .accessLevel(m.getAccessLevel()) // enum -> enum (đúng kiểu)
                                .trailerURL(m.getTrailerURL())
                                .videoURL(m.getVideoURL())
                                .genres(
                                                m.getGenres().stream()
                                                                .map(g -> new GenreDTO(g.getGenreID(), g.getName()))
                                                                .collect(Collectors.toList()))
                                .build();
        }

        private Long currentUserIdOrNull() {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth == null)
                        return null;
                Object principal = auth.getPrincipal();
                if (principal instanceof CustomUserDetails cud)
                        return cud.getId();
                return null;
        }

        private void ensurePlayable(Movie movie, Long userId) {
                if (movie.getAccessLevel() == AccessLevel.PREMIUM) {
                        if (userId == null || !subscriptionService.hasPremiumAccess(userId)) {
                                throw new RuntimeException("Bạn cần gói PREMIUM để xem phim này");
                        }
                }
        }

        // ---------- CRUD ----------
        @Override
        public List<MovieDTO> getAllMovies() {
                return movieRepository.findAll().stream().map(this::toDTO).toList();
        }

        @Override
        public MovieDTO getMovieById(Long id) {
                return movieRepository.findById(id).map(this::toDTO)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim"));
        }

        @Override
        public MovieDTO createMovie(MovieRequestDTO dto) {
                Movie movie = Movie.builder()
                                .title(dto.getTitle())
                                .description(dto.getDescription())
                                .duration(dto.getDuration())
                                .year(dto.getYear())
                                .poster(dto.getPoster())
                                .accessLevel(dto.getAccessLevel()) // dto giữ kiểu AccessLevel
                                .trailerURL(dto.getTrailerURL())
                                .videoURL(dto.getVideoURL())
                                .build();

                if (dto.getGenreIds() != null) {
                        Set<Genre> genres = dto.getGenreIds().stream()
                                        .map(gid -> genreRepository.findById(gid)
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "Genre không tồn tại: " + gid)))
                                        .collect(Collectors.toSet());
                        movie.setGenres(genres);
                }
                return toDTO(movieRepository.save(movie));
        }

        @Override
        public MovieDTO updateMovie(Long id, MovieRequestDTO dto) {
                Movie movie = movieRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim"));

                movie.setTitle(dto.getTitle());
                movie.setDescription(dto.getDescription());
                movie.setDuration(dto.getDuration());
                movie.setYear(dto.getYear());
                movie.setPoster(dto.getPoster());
                movie.setAccessLevel(dto.getAccessLevel()); // enum
                movie.setTrailerURL(dto.getTrailerURL());
                movie.setVideoURL(dto.getVideoURL());

                if (dto.getGenreIds() != null) {
                        Set<Genre> genres = dto.getGenreIds().stream()
                                        .map(gid -> genreRepository.findById(gid)
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "Genre không tồn tại: " + gid)))
                                        .collect(Collectors.toSet());
                        movie.setGenres(genres);
                }

                return toDTO(movieRepository.save(movie));
        }

        @Override
        public void deleteMovie(Long id) {
                movieRepository.deleteById(id);
        }

        // ---------- Genres ----------
        @Override
        public void assignGenresToMovie(Long movieId, List<Long> genreIds) {
                Movie movie = movieRepository.findById(movieId)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim"));
                Set<Genre> genres = genreIds.stream()
                                .map(gid -> genreRepository.findById(gid)
                                                .orElseThrow(() -> new RuntimeException("Genre không tồn tại: " + gid)))
                                .collect(Collectors.toSet());
                movie.setGenres(genres);
                movieRepository.save(movie);
        }

        @Override
        public void addGenreToMovie(Long movieId, Long genreId) {
                Movie movie = movieRepository.findById(movieId)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim"));
                Genre genre = genreRepository.findById(genreId)
                                .orElseThrow(() -> new RuntimeException("Genre không tồn tại: " + genreId));
                movie.getGenres().add(genre);
                movieRepository.save(movie);
        }

        @Override
        public void removeGenreFromMovie(Long movieId, Long genreId) {
                Movie movie = movieRepository.findById(movieId)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim"));
                Genre genre = genreRepository.findById(genreId)
                                .orElseThrow(() -> new RuntimeException("Genre không tồn tại: " + genreId));
                movie.getGenres().remove(genre);
                movieRepository.save(movie);
        }

        @Override
        public List<Genre> getGenresByMovie(Long movieId) {
                return movieRepository.findById(movieId)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim"))
                                .getGenres().stream().toList();
        }

        // ---------- ACL ----------
        @Override
        public MovieDTO getMoviePlayInfo(Long movieId, Long userId) {
                Movie movie = movieRepository.findById(movieId)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim"));
                ensurePlayable(movie, userId);
                return toDTO(movie);
        }

        // ---------- Filter ----------
        @Override
        public List<MovieDTO> getMoviesByGenreId(Long genreId) {
                return movieRepository.findAll().stream()
                                .filter(m -> m.getGenres().stream().anyMatch(g -> g.getGenreID().equals(genreId)))
                                .map(this::toDTO)
                                .toList();
        }

        @Override
        public List<MovieDTO> getMoviesByGenreIds(List<Long> genreIds) {
                return movieRepository.findAll().stream()
                                .filter(m -> m.getGenres().stream().anyMatch(g -> genreIds.contains(g.getGenreID())))
                                .map(this::toDTO)
                                .toList();
        }

        // ---------- Streaming (proxy HTTP hoặc file local) ----------
        @Override
        public void streamVideo(Long movieId, HttpServletRequest request, HttpServletResponse response) {
                Movie movie = movieRepository.findById(movieId)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim"));

                // Kiểm tra quyền xem
                ensurePlayable(movie, currentUserIdOrNull());

                String src = movie.getVideoURL();
                if (!StringUtils.hasText(src)) {
                        throw new RuntimeException("Phim chưa có videoURL");
                }

                // Nếu là HTTP(S) (Dropbox, v.v.) -> proxy
                if (src.startsWith("http://") || src.startsWith("https://")) {
                        // Nếu là Dropbox URL, convert thành streaming URL
                        String streamingUrl = src;
                        if (src.contains("dropbox.com")) {
                                streamingUrl = convertDropboxToStreamingUrl(src);
                        }
                        proxyHttpVideo(streamingUrl, request, response);
                        return;
                }

                // Nếu là file local -> stream có hỗ trợ Range
                streamLocalFile(src, request, response);
        }

        // Kiểm tra xem URL có phải là Dropbox URL hợp lệ không
        private boolean isValidDropboxUrl(String url) {
                return url != null && url.contains("dropbox.com") &&
                                (url.contains("/scl/fi/") || url.contains("/s/"));
        }

        // Convert Dropbox sharing URL thành direct streaming URL
        private String convertDropboxToStreamingUrl(String dropboxUrl) {
                try {
                        if (!isValidDropboxUrl(dropboxUrl)) {
                                System.out.println("URL không phải là Dropbox URL hợp lệ: " + dropboxUrl);
                                return dropboxUrl;
                        }

                        // Dropbox sharing URL format:
                        // https://www.dropbox.com/scl/fi/xyz/filename?rlkey=...&st=...&dl=0
                        // Convert thành direct URL:
                        // https://dl.dropboxusercontent.com/scl/fi/xyz/filename?rlkey=...&st=...

                        if (dropboxUrl.contains("www.dropbox.com")) {
                                // Loại bỏ tất cả dl parameters
                                String cleanUrl = dropboxUrl.replaceAll("[&?]dl=[01]", "");

                                // Convert domain từ www.dropbox.com sang dl.dropboxusercontent.com
                                String directUrl = cleanUrl.replace("www.dropbox.com", "dl.dropboxusercontent.com");

                                System.out.println("✅ Dropbox URL Conversion:");
                                System.out.println("   Original: " + dropboxUrl);
                                System.out.println("   Direct: " + directUrl);

                                return directUrl;
                        }

                        return dropboxUrl;
                } catch (Exception e) {
                        System.err.println("❌ Error converting Dropbox URL: " + e.getMessage());
                        e.printStackTrace();
                        return dropboxUrl;
                }
        }

        // Proxy qua HTTP(S) và forward Range nếu có
        private void proxyHttpVideo(String urlStr, HttpServletRequest request, HttpServletResponse response) {
                HttpURLConnection conn = null;
                try {
                        URL url = new URL(urlStr); // <- cần import java.net.URL
                        conn = (HttpURLConnection) url.openConnection();

                        // Set headers để Dropbox serve video content thay vì HTML
                        conn.setRequestProperty("User-Agent",
                                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
                        conn.setRequestProperty("Accept", "video/mp4,video/*,*/*;q=0.9");
                        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
                        conn.setRequestProperty("Accept-Encoding", "identity");
                        conn.setRequestProperty("Connection", "keep-alive");

                        // Forward Range để trình duyệt có thể tua
                        String range = request.getHeader("Range");
                        if (range != null) {
                                conn.setRequestProperty("Range", range);
                        }

                        // Thêm timeout để tránh hang
                        conn.setConnectTimeout(10000); // 10 seconds
                        conn.setReadTimeout(30000); // 30 seconds

                        conn.connect();

                        int code = conn.getResponseCode();

                        // Debug logging
                        System.out.println("Proxying URL: " + urlStr);
                        System.out.println("Response Code: " + code);
                        System.out.println("Content-Type: " + conn.getContentType());

                        // Kiểm tra nếu nhận được HTML thay vì video
                        String contentType = conn.getContentType();
                        if (contentType != null && contentType.contains("text/html")) {
                                System.err.println("Error: Received HTML instead of video content");
                                System.err.println("URL: " + urlStr);
                                System.err.println("Content-Type: " + contentType);
                                throw new RuntimeException(
                                                "URL không hợp lệ cho streaming video. Vui lòng kiểm tra lại URL Dropbox.");
                        }

                        // Kiểm tra response code
                        if (code >= 400) {
                                System.err.println("HTTP Error " + code + " when accessing: " + urlStr);
                                throw new RuntimeException("Không thể truy cập video: HTTP " + code);
                        }

                        // Thiết lập status phù hợp (206 nếu partial)
                        response.setStatus(
                                        code == HttpURLConnection.HTTP_PARTIAL ? HttpServletResponse.SC_PARTIAL_CONTENT
                                                        : HttpServletResponse.SC_OK);

                        // Content-Type: xử lý đúng cho video streaming
                        String ct = conn.getContentType();
                        if (ct != null && (ct.contains("video/") || ct.contains("application/octet-stream")
                                        || ct.contains("application/binary"))) {
                                // Nếu là video content hoặc binary, set thành video/mp4 để browser hiểu
                                response.setContentType("video/mp4");
                        } else if (ct != null && !ct.isBlank() && !ct.contains("text/html")) {
                                response.setContentType(ct);
                        } else {
                                response.setContentType("video/mp4");
                        }

                        // Copy các header quan trọng cho seek
                        String contentRange = conn.getHeaderField("Content-Range");
                        if (contentRange != null) {
                                response.setHeader("Content-Range", contentRange);
                        }
                        String contentLength = conn.getHeaderField("Content-Length");
                        if (contentLength != null) {
                                response.setHeader("Content-Length", contentLength);
                        }
                        // Headers quan trọng cho video streaming
                        response.setHeader("Accept-Ranges", "bytes");
                        response.setHeader("Content-Disposition", "inline; filename=video.mp4");
                        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                        response.setHeader("Pragma", "no-cache");
                        response.setHeader("Expires", "0");

                        // CORS headers nếu cần
                        response.setHeader("Access-Control-Allow-Origin", "*");
                        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, OPTIONS");
                        response.setHeader("Access-Control-Allow-Headers", "Range");

                        try (InputStream in = conn.getInputStream(); OutputStream out = response.getOutputStream()) {
                                byte[] buf = new byte[8192];
                                int r;
                                while ((r = in.read(buf)) != -1) {
                                        out.write(buf, 0, r);
                                }
                                out.flush();
                        }
                } catch (IOException e) {
                        throw new RuntimeException("Lỗi khi proxy video: " + e.getMessage(), e);
                } finally {
                        if (conn != null)
                                conn.disconnect();
                }
        }

        // Stream file local với Range
        private void streamLocalFile(String path, HttpServletRequest request, HttpServletResponse response) {
                File file = new File(path);
                if (!file.exists())
                        throw new RuntimeException("File video không tồn tại: " + path);

                try (RandomAccessFile raf = new RandomAccessFile(file, "r");
                                OutputStream os = response.getOutputStream()) {
                        long fileLen = file.length();
                        String range = request.getHeader("Range");

                        long start = 0, end = fileLen - 1;
                        if (range != null && range.startsWith("bytes=")) {
                                String[] parts = range.substring(6).split("-");
                                try {
                                        start = Long.parseLong(parts[0]);
                                        if (parts.length > 1 && !parts[1].isEmpty())
                                                end = Long.parseLong(parts[1]);
                                } catch (NumberFormatException ignored) {
                                }
                        }
                        if (end >= fileLen)
                                end = fileLen - 1;
                        if (start > end)
                                start = 0;

                        long contentLength = end - start + 1;

                        response.setStatus(range == null ? HttpServletResponse.SC_OK
                                        : HttpServletResponse.SC_PARTIAL_CONTENT);
                        response.setContentType("video/mp4");
                        response.setHeader("Accept-Ranges", "bytes");
                        response.setHeader("Content-Range", String.format("bytes %d-%d/%d", start, end, fileLen));
                        response.setHeader("Content-Length", String.valueOf(contentLength));

                        raf.seek(start);
                        byte[] buffer = new byte[8192];
                        long left = contentLength;
                        while (left > 0) {
                                int read = raf.read(buffer, 0, (int) Math.min(buffer.length, left));
                                if (read == -1)
                                        break;
                                os.write(buffer, 0, read);
                                left -= read;
                        }
                } catch (IOException e) {
                        throw new RuntimeException("Lỗi khi stream video local", e);
                }
        }
}
