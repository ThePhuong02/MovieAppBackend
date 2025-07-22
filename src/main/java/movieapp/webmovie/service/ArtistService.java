package movieapp.webmovie.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import movieapp.webmovie.entity.Artist;
import movieapp.webmovie.repository.ArtistRepository;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    public List<Artist> getAll() {
        return artistRepository.findAll();
    }

    public Artist getById(Long id) {
        return artistRepository.findById(id).orElse(null);
    }

    public Artist create(Artist artist) {
        return artistRepository.save(artist);
    }

    public Artist update(Long id, Artist newData) {
        Artist artist = artistRepository.findById(id).orElse(null);
        if (artist != null) {
            artist.setName(newData.getName());
            artist.setBio(newData.getBio());
            return artistRepository.save(artist);
        }
        return null;
    }

    public void delete(Long id) {
        artistRepository.deleteById(id);
    }
}
