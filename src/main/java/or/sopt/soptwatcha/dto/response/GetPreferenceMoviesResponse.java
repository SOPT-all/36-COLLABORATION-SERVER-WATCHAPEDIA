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

    private List<GetPreferenceMoviesKeywordResponseDTO> keyword;

    public static GetPreferenceMoviesResponse of(Movie movie, MovieImage movieImage, List<GetPreferenceMoviesKeywordResponseDTO> movieKeyword) {
        return GetPreferenceMoviesResponse.builder()
                .id(movie.getId())
                .imagePath(movieImage.getImageLink())
                .title(movie.getTitle())
                .score(movie.getScore())
                .keyword(movieKeyword)
                .build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetPreferenceMoviesGroupDTO {
        private String description;
        private String keyword;
        private List<GetPreferenceMoviesResponse> movies;

        public static GetPreferenceMoviesGroupDTO of(String description, List<GetPreferenceMoviesResponse> movies,Keyword keyword) {

            return GetPreferenceMoviesGroupDTO.builder()
                    .description(description)
                    .keyword(keyword.getValue())
                    .movies(movies)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetPreferenceMoviesResponseWrapper {
        private List<GetPreferenceMoviesGroupDTO> preferenceMovies;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetPreferenceMoviesKeywordResponseDTO {

        private String keyword;

        public static GetPreferenceMoviesKeywordResponseDTO of(Keyword keyword) {
            return GetPreferenceMoviesKeywordResponseDTO.builder()
                    .keyword(keyword.getValue())
                    .build();
        }
    }

}
