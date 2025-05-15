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

    private List<List<GetPreferenceMoviesResponse>> result;

    public static GetPreferenceMoviesListResponse of(List<List<GetPreferenceMoviesResponse>> responses) {
        return GetPreferenceMoviesListResponse.builder()
                .result(responses)
                .build();
    }
}