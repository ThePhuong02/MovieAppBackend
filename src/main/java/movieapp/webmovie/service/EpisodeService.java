package movieapp.webmovie.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import movieapp.webmovie.dto.EpisodeDTO;
import movieapp.webmovie.entity.Episode;
import movieapp.webmovie.entity.Series;
import movieapp.webmovie.repository.EpisodeRepository;
import movieapp.webmovie.repository.SeriesRepository;

@Service
public class EpisodeService {

    @Autowired
    private EpisodeRepository repo;
    @Autowired
    private SeriesRepository seriesRepo;

    public List<Episode> getAll() {
        return repo.findAll();
    }

    public Episode getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public List<Episode> getBySeriesId(Long seriesId) {
        return repo.findBySeries_SeriesID(seriesId);
    }

    public Episode create(EpisodeDTO dto) {
        Series series = seriesRepo.findById(dto.getSeriesID()).orElse(null);
        if (series == null)
            return null;

        Episode ep = new Episode();
        ep.setTitle(dto.getTitle());
        ep.setDescription(dto.getDescription());
        ep.setDuration(dto.getDuration());
        ep.setEpisodeNumber(dto.getEpisodeNumber());
        ep.setVideoURL(dto.getVideoURL());
        ep.setSeries(series);
        return repo.save(ep);
    }

    public Episode update(Long id, EpisodeDTO dto) {
        Optional<Episode> existing = repo.findById(id);
        Series series = seriesRepo.findById(dto.getSeriesID()).orElse(null);
        if (existing.isPresent() && series != null) {
            Episode ep = existing.get();
            ep.setTitle(dto.getTitle());
            ep.setDescription(dto.getDescription());
            ep.setDuration(dto.getDuration());
            ep.setEpisodeNumber(dto.getEpisodeNumber());
            ep.setVideoURL(dto.getVideoURL());
            ep.setSeries(series);
            return repo.save(ep);
        }
        return null;
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
