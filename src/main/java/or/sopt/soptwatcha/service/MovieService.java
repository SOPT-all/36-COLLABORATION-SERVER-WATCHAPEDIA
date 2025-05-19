package or.sopt.soptwatcha.service;

import or.sopt.soptwatcha.dto.response.GetMovieTopRankingResponseDTO;
import or.sopt.soptwatcha.dto.response.GetPreferenceMoviesListResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface MovieService {
    GetPreferenceMoviesListResponse getPreferenceMovies(Long commentId);

    @Transactional(readOnly = true)
    GetMovieTopRankingResponseDTO.GetMovieTopRankingResponseListDTO getMovieTopRanking();
}
