package or.sopt.soptwatcha.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import or.sopt.soptwatcha.domain.Keyword;
import or.sopt.soptwatcha.domain.Movie;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetMovieTopRankingResponseDTO {

    private String movieImagePath;

    private String movieTitle;

    private double expectScore;

    private List<String> movieKeywordList;

    public static GetMovieTopRankingResponseDTO from(Movie movie,String movieImagePaths, List<String> keywords) {

        return GetMovieTopRankingResponseDTO.builder()
                .movieImagePath(movieImagePaths)
                .movieTitle(movie.getTitle())
                .expectScore(movie.getExpectScore())
                .movieKeywordList(keywords)
                .build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetMovieTopRankingResponseListDTO{

        private List<GetMovieTopRankingResponseDTO> result;

    }
}
