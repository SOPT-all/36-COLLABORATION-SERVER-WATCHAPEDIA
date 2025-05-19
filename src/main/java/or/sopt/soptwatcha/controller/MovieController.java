package or.sopt.soptwatcha.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import or.sopt.soptwatcha.common.response.BaseResponse;
import or.sopt.soptwatcha.dto.response.GetMovieSoonResponseDTO;
import or.sopt.soptwatcha.dto.response.GetMovieTopRankingResponseDTO;
import or.sopt.soptwatcha.dto.response.GetPreferenceMoviesListResponse;
import or.sopt.soptwatcha.dto.response.GetPreferenceMoviesResponse;
import or.sopt.soptwatcha.dto.response.MovieDetailResponseDTO;
import or.sopt.soptwatcha.service.MovieService;
import or.sopt.soptwatcha.service.MovieServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "영화 관련 API")
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/{postId}")
    @Operation(summary = "영화 상세 정보 조회 API")
    public BaseResponse<MovieDetailResponseDTO> getMovieDetail(@PathVariable Long postId) {
        MovieDetailResponseDTO response = movieService.getMovieDetail(postId);
        return BaseResponse.ok(response);
    }


    @GetMapping("/preference/{commentId}")
    @Operation(summary = "코멘트를 남긴 영화 기반 추천 API")
    public BaseResponse<GetPreferenceMoviesResponse.GetPreferenceMoviesResponseWrapper> getPreferenceMovies(@PathVariable Long commentId) {

        GetPreferenceMoviesResponse.GetPreferenceMoviesResponseWrapper result = movieService.getPreferenceMoviesV2(commentId);

        return BaseResponse.ok(result);
    }


    @GetMapping("/ranking")
    @Operation(summary = "내가 좋아할 만한 작품 랭킹 API",
    description = "작품에 따른 예상 별점으로 다섯개의 작품 리스트를 반환합니다")
    public BaseResponse<GetMovieTopRankingResponseDTO.GetMovieTopRankingResponseListDTO> getRankingMovies() {

        GetMovieTopRankingResponseDTO.GetMovieTopRankingResponseListDTO result = movieService.getMovieTopRanking();

        return BaseResponse.ok(result);
    }


    @GetMapping("/soon")
    @Operation(summary = "개봉 예정작 조회 API",
    description = "개봉예정일과 가장 가까운 작품 다섯개를 리스트로 반환합니다 <br><br> **movieType**에는 영화라면 MOVIE,시리즈라면 SERIES 를 넣어주세요")
    public BaseResponse<GetMovieSoonResponseDTO.GetMovieSoonResponseListDTO> getSoonMovies(@RequestParam String movieType) {

        GetMovieSoonResponseDTO.GetMovieSoonResponseListDTO result = movieService.getSoon(movieType);

        return BaseResponse.ok(result);
    }
}
