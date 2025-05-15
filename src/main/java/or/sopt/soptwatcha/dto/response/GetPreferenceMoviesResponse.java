package or.sopt.soptwatcha.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import or.sopt.soptwatcha.domain.Movie;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPreferenceMoviesResponse {

    private String title;

    public static GetPreferenceMoviesResponse of(Movie movie) {
        return GetPreferenceMoviesResponse.builder()
                .title(movie.getTitle())
                .build();
    }
}
