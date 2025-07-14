package movieapp.webmovie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import movieapp.webmovie.entity.Artist;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {}

