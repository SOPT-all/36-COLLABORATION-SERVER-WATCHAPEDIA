package or.sopt.soptwatcha.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import or.sopt.soptwatcha.common.response.BaseResponse;
import or.sopt.soptwatcha.dto.response.MovieDetailResponseDTO;
import or.sopt.soptwatcha.service.MovieServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "영화 관련 API")
public class MovieController {

    private final MovieServiceImpl movieService;

    @Operation(summary = "영화 상세 정보 조회 API")
    @GetMapping("/{postId}")
    public BaseResponse<MovieDetailResponseDTO> getMovieDetail(@PathVariable Long postId) {
        MovieDetailResponseDTO response = movieService.getMovieDetail(postId);
        return BaseResponse.ok(response);
    }
}