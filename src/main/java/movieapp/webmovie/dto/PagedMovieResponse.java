package movieapp.webmovie.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedMovieResponse {
    private List<MovieDTO> data;
    private int totalPages;
    private int currentPage;
    private long totalItems;
}
