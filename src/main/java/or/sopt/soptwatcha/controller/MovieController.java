package or.sopt.soptwatcha.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import or.sopt.soptwatcha.common.response.BaseResponse;
import or.sopt.soptwatcha.dto.response.GetMovieSoonResponseDTO;
import or.sopt.soptwatcha.dto.response.GetMovieTopRankingResponseDTO;
import or.sopt.soptwatcha.dto.response.GetPreferenceMoviesListResponse;
import or.sopt.soptwatcha.service.MovieService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Tag(name = "영화 관련 API")
public class MovieController {


    private final MovieService movieService;


    @GetMapping("posts/preference/{commentId}")
    @Operation(summary = "코멘트를 남긴 영화 기반 추천 API")
    public BaseResponse<GetPreferenceMoviesListResponse> getPreferenceMovies(@PathVariable Long commentId) {

        GetPreferenceMoviesListResponse result = movieService.getPreferenceMovies(commentId);

        return BaseResponse.ok(result);
    }


    @GetMapping("posts/ranking")
    @Operation(summary = "내가 좋아할 만한 작품 랭킹 API")
    public BaseResponse<GetMovieTopRankingResponseDTO.GetMovieTopRankingResponseListDTO> getRankingMovies() {

        GetMovieTopRankingResponseDTO.GetMovieTopRankingResponseListDTO result = movieService.getMovieTopRanking();

        return BaseResponse.ok(result);
    }


    @GetMapping("posts/soon")
    @Operation(summary = "개봉 예정작 조회 API")
    public BaseResponse<GetMovieSoonResponseDTO.GetMovieSoonResponseListDTO> getSoonMovies(@RequestParam String movieType) {

        GetMovieSoonResponseDTO.GetMovieSoonResponseListDTO result = movieService.getSoon(movieType);

        return BaseResponse.ok(result);
    }
}
