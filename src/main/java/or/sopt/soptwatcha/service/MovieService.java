package or.sopt.soptwatcha.service;

import or.sopt.soptwatcha.dto.response.GetPreferenceMoviesListResponse;
import org.springframework.stereotype.Service;

public interface MovieService {
    GetPreferenceMoviesListResponse getPreferenceMovies(Long commentId);
}
