package movieapp.webmovie.converter;

import movieapp.webmovie.dto.MovieResponseDTO;
import movieapp.webmovie.entity.Movie;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MovieConverter {
    @Autowired
    private ModelMapper modelMapper;

    public MovieResponseDTO movieResponseDTO(Movie it){
        MovieResponseDTO movieResponse = modelMapper.map(it, MovieResponseDTO.class);
        return movieResponse;
    }
}
