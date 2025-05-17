package movieapp.webmovie.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Movie information")
public class Movie {
    
    @Schema(description = "Unique identifier of the movie", example = "1")
    private Long id;
    
    @Schema(description = "Title of the movie", example = "The Shawshank Redemption")
    private String title;
    
    @Schema(description = "Description of the movie", example = "Two imprisoned men bond over a number of years...")
    private String description;
    
    @Schema(description = "Release year of the movie", example = "1994")
    private Integer year;
    
    @Schema(description = "Duration of the movie in minutes", example = "142")
    private Integer duration;
    
    @Schema(description = "URL to the movie poster", example = "https://example.com/poster.jpg")
    private String poster;
    
    @Schema(description = "Genre of the movie", example = "Drama")
    private String genre;
} 