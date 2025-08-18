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
import movieapp.webmovie.repository.SubscriptionRepository;
import movieapp.webmovie.security.CustomUserDetails;
import movieapp.webmovie.service.MovieService;
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
        private final SubscriptionRepository subscriptionRepository;

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
                        if (userId == null || !subscriptionRepository.existsByUserIdAndIsActiveTrue(userId)) {
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
                        proxyHttpVideo(src, request, response);
                        return;
                }

                // Nếu là file local -> stream có hỗ trợ Range
                streamLocalFile(src, request, response);
        }

        // Proxy qua HTTP(S) và forward Range nếu có
        private void proxyHttpVideo(String urlStr, HttpServletRequest request, HttpServletResponse response) {
                HttpURLConnection conn = null;
                try {
                        URL url = new URL(urlStr); // <- cần import java.net.URL
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestProperty("User-Agent", "Mozilla/5.0");

                        // Forward Range để trình duyệt có thể tua
                        String range = request.getHeader("Range");
                        if (range != null) {
                                conn.setRequestProperty("Range", range);
                        }
                        conn.connect();

                        int code = conn.getResponseCode();
                        // Thiết lập status phù hợp (206 nếu partial)
                        response.setStatus(
                                        code == HttpURLConnection.HTTP_PARTIAL ? HttpServletResponse.SC_PARTIAL_CONTENT
                                                        : HttpServletResponse.SC_OK);

                        // Content-Type: nếu thiếu, fallback video/mp4
                        String ct = conn.getContentType();
                        response.setContentType((ct != null && !ct.isBlank()) ? ct : "video/mp4");

                        // Copy các header quan trọng cho seek
                        String contentRange = conn.getHeaderField("Content-Range");
                        if (contentRange != null) {
                                response.setHeader("Content-Range", contentRange);
                        }
                        String contentLength = conn.getHeaderField("Content-Length");
                        if (contentLength != null) {
                                response.setHeader("Content-Length", contentLength);
                        }
                        // cho phép bytes
                        response.setHeader("Accept-Ranges", "bytes");
                        response.setHeader("Content-Disposition", "inline; filename=video.mp4");

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
