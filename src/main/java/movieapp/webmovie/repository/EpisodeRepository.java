package movieapp.webmovie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import movieapp.webmovie.entity.Episode;

public interface EpisodeRepository extends JpaRepository<Episode, Integer> {
    List<Episode> findBySeries_SeriesID(Integer seriesId);
}