package movieapp.webmovie.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import movieapp.webmovie.entity.Series;
import movieapp.webmovie.repository.SeriesRepository;
@Service
public class SeriesService {
      @Autowired private SeriesRepository repo;

    public List<Series> getAll() {
        return repo.findAll();
    }

    public Series create(Series s) {
        return repo.save(s);
    }

    public Series update(Integer id, Series s) {
        Optional<Series> existing = repo.findById(id);
        if (existing.isPresent()) {
            Series old = existing.get();
            old.setTitle(s.getTitle());
            old.setDescription(s.getDescription());
            old.setPoster(s.getPoster());
            old.setYear(s.getYear());
            return repo.save(old);
        }
        return null;
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
