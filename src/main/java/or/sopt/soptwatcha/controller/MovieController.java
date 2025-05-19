package or.sopt.soptwatcha.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import or.sopt.soptwatcha.common.response.BaseResponse;
import or.sopt.soptwatcha.dto.response.GetMovieSoonResponseDTO;
import or.sopt.soptwatcha.dto.response.GetMovieTopRankingResponseDTO;
import or.sopt.soptwatcha.dto.response.GetPreferenceMoviesResponse;
import or.sopt.soptwatcha.service.MovieService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Tag(name = "영화 관련 API")
public class MovieController {


    private final MovieService movieService;


    @GetMapping("posts/preference/{commentId}")
    @Operation(summary = "코멘트를 남긴 영화 기반 추천 API",
    description = "입력된 댓글 식별자를 통해 **댓글**을 찾습니다 <br><br>" +
            "댓글을 통해 해당 댓글의 **키워드**를 찾습니다. 키워드는 긍부정을 분리하고, 상위 키워드에 따라 우선순위대로 재배치됩니다 <br><br>" +
            "재정렬된 키워드에 따라, 같은 키워드를 입력한 댓글을 찾습니다 <br><br>" +
            "최종적으로 **해당 댓글이 평가한 작품을 다섯개씩** 찾아 이중 리스트로 반환합니다 <br><br><br>" +
            "파라미터에 **이전 페이지에서 작성된 코멘트**의 식별자를 넣어주세요 <br><br>" +
            "cf) 긍정 키워드가 하나도 없는 경우, **KEYWORD_NOT_FOUND** 예외를 반환합니다. 이는 추후 특정 로직으로 리팩터링 될 예정입니다")
    public BaseResponse<GetPreferenceMoviesResponse.GetPreferenceMoviesResponseWrapper> getPreferenceMovies(@PathVariable Long commentId) {

        GetPreferenceMoviesResponse.GetPreferenceMoviesResponseWrapper result = movieService.getPreferenceMoviesV2(commentId);

        return BaseResponse.ok(result);
    }


    @GetMapping("posts/ranking")
    @Operation(summary = "내가 좋아할 만한 작품 랭킹 API",
    description = "작품에 따른 예상 별점으로 다섯개의 작품 리스트를 반환합니다")
    public BaseResponse<GetMovieTopRankingResponseDTO.GetMovieTopRankingResponseListDTO> getRankingMovies() {

        GetMovieTopRankingResponseDTO.GetMovieTopRankingResponseListDTO result = movieService.getMovieTopRanking();

        return BaseResponse.ok(result);
    }


    @GetMapping("posts/soon")
    @Operation(summary = "개봉 예정작 조회 API",
    description = "개봉예정일과 가장 가까운 작품 다섯개를 리스트로 반환합니다 <br><br> **movieType**에는 영화라면 **MOVIE**,시리즈라면 **SERIES** 를 넣어주세요")
    public BaseResponse<GetMovieSoonResponseDTO.GetMovieSoonResponseListDTO> getSoonMovies(@RequestParam String movieType) {

        GetMovieSoonResponseDTO.GetMovieSoonResponseListDTO result = movieService.getSoon(movieType);

        return BaseResponse.ok(result);
    }
}
