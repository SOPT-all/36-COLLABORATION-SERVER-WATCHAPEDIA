package or.sopt.soptwatcha.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import or.sopt.soptwatcha.dto.response.GetMovieTopRankingResponseDTO;
import or.sopt.soptwatcha.dto.response.GetPreferenceMoviesListResponse;
import or.sopt.soptwatcha.service.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Tag(name = "영화 관련 API")
public class MovieController {

    private final MovieService movieService;


    @GetMapping("posts/preference/{commentId}")
    @Operation(summary = "코멘트를 남긴 영화 기반 추천 API")
    public GetPreferenceMoviesListResponse getPreferenceMovies(@PathVariable Long commentId) {

        return movieService.getPreferenceMovies(commentId);
    }

    @GetMapping("posts/ranking")
    @Operation(summary = "내가 좋아할 만한 작품 랭킹 API")
    public GetMovieTopRankingResponseDTO.GetMovieTopRankingResponseListDTO getRankingMovies() {

        return movieService.getMovieTopRanking();
    }
}
