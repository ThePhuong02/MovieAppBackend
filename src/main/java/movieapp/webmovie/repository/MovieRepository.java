package movieapp.webmovie.repository;

import movieapp.webmovie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // ✅ Lấy phim theo 1 thể loại
    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.genreID = :genreId")
    List<Movie> findByGenreId(@Param("genreId") Long genreId);

    // ✅ Lấy phim theo nhiều thể loại (chỉ cần chứa ít nhất 1 thể loại)
    @Query("SELECT DISTINCT m FROM Movie m JOIN m.genres g WHERE g.genreID IN :genreIds")
    List<Movie> findByGenreIds(@Param("genreIds") List<Long> genreIds);

    // ✅ Tìm kiếm phim theo title và description với phân trang
    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(m.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Movie> searchMovies(@Param("query") String query, Pageable pageable);
}
