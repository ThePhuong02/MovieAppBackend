package movieapp.webmovie.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import movieapp.webmovie.dto.SeriesDTO;
import movieapp.webmovie.entity.Series;
import movieapp.webmovie.repository.SeriesRepository;

@Service
public class SeriesService {

    @Autowired
    private SeriesRepository repo;

    public List<Series> getAll() {
        return repo.findAll();
    }

    public Series create(SeriesDTO dto) {
        Series s = new Series();
        s.setTitle(dto.getTitle());
        s.setDescription(dto.getDescription());
        s.setPoster(dto.getPoster());
        s.setYear(dto.getYear());
        return repo.save(s);
    }

    public Series update(Long id, SeriesDTO dto) {
        Optional<Series> existing = repo.findById(id);
        if (existing.isPresent()) {
            Series s = existing.get();
            s.setTitle(dto.getTitle());
            s.setDescription(dto.getDescription());
            s.setPoster(dto.getPoster());
            s.setYear(dto.getYear());
            return repo.save(s);
        }
        return null;
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
