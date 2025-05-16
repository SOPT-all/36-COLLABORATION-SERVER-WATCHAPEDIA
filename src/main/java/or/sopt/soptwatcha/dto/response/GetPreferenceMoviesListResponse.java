package or.sopt.soptwatcha.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import or.sopt.soptwatcha.domain.Movie;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPreferenceMoviesListResponse {

    private List<KeywordRecommendationGroupResponse> preferenceMovies;

    public static GetPreferenceMoviesListResponse of(List<KeywordRecommendationGroupResponse> groups) {
        return GetPreferenceMoviesListResponse.builder()
                .preferenceMovies(groups)
                .build();
    }
}