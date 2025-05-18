package or.sopt.soptwatcha.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import or.sopt.soptwatcha.domain.Keyword;
import or.sopt.soptwatcha.domain.Movie;
import or.sopt.soptwatcha.domain.MovieImage;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPreferenceMoviesResponse {

    private Long id;

    private String imagePath;

    private String title;

    private double score;

    private List<KeywordResponseDTO> keyword;

    public static GetPreferenceMoviesResponse of(Movie movie, MovieImage movieImage, List<KeywordResponseDTO> movieKeyword) {
        return GetPreferenceMoviesResponse.builder()
                .id(movie.getId())
                .imagePath(movieImage.getImageLink())
                .title(movie.getTitle())
                .score(movie.getScore())
                .keyword(movieKeyword)
                .build();
    }
}
