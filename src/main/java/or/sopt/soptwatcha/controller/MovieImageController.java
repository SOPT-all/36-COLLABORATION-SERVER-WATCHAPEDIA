package or.sopt.soptwatcha.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import or.sopt.soptwatcha.domain.MovieImage;
import or.sopt.soptwatcha.service.MovieImageServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/movie-image")
@RequiredArgsConstructor
@Tag(name = "영화 포스터 관련 API (테스트용 API 입니다)")
public class MovieImageController {

    private final MovieImageServiceImpl movieImageService;

    @Operation(summary = "영화 포스터 업로드 API")
    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String save(@RequestParam("file") MultipartFile file,
                       @RequestParam("movieId") Long movieId) {

        return movieImageService.uploadImage(file,movieId);
    }


    @GetMapping
    @Operation(summary = "영화 포스터 조회 API",
    description = "현재는 filename으로 조회하고 있지만, 추후 영화/시리즈 객체를 이용하여 한 번에 조회 할 수 있도록 업데이트 될 예정입니다")
    public String getImage(String fileName){
        return movieImageService.findByFileName(fileName);
    }
}
