package movieapp.webmovie.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import movieapp.webmovie.entity.Episode;
import movieapp.webmovie.repository.EpisodeRepository;

@Service
public class EpisodeService {
    @Autowired private EpisodeRepository repo;

    public List<Episode> getAll() {
        return repo.findAll();
    }

    public List<Episode> getBySeriesId(Integer seriesId) {
        return repo.findBySeries_SeriesID(seriesId);
    }

    public Episode create(Episode ep) {
        return repo.save(ep);
    }

    public Episode update(Integer id, Episode ep) {
        Optional<Episode> existing = repo.findById(id);
        if (existing.isPresent()) {
            Episode old = existing.get();
            old.setTitle(ep.getTitle());
            old.setDescription(ep.getDescription());
            old.setDuration(ep.getDuration());
            old.setEpisodeNumber(ep.getEpisodeNumber());
            old.setVideoURL(ep.getVideoURL());
            old.setSeries(ep.getSeries());
            return repo.save(old);
        }
        return null;
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
