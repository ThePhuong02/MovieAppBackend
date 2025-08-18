package movieapp.webmovie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import movieapp.webmovie.entity.MovieArtist;
import movieapp.webmovie.entity.MovieArtistId;

public interface MovieArtistRepository extends JpaRepository<MovieArtist, MovieArtistId> {
    List<MovieArtist> findByIdMovieID(Long movieID);

    List<MovieArtist> findByIdArtistID(Long artistID);
}
