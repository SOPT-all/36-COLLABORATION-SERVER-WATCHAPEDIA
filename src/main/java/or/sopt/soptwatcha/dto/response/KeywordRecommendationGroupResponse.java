package or.sopt.soptwatcha.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeywordRecommendationGroupResponse {

    private String description;
    private List<GetPreferenceMoviesResponse> movies;
}