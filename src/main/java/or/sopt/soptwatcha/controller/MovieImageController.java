package or.sopt.soptwatcha.controller;

import lombok.RequiredArgsConstructor;
import or.sopt.soptwatcha.domain.MovieImage;
import or.sopt.soptwatcha.service.MovieImageServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/movie-image")
@RequiredArgsConstructor
public class MovieImageController {

    private final MovieImageServiceImpl movieImageService;

    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String save(@RequestParam("file") MultipartFile file,
                       @RequestParam("movieId") Long movieId) {
        MovieImage uploadedImage = movieImageService.uploadImage("영화포스터",file,movieId);
        return uploadedImage.getImageLink();
    }


    @GetMapping
    public String getImage(String fileName){
        return movieImageService.findByFileName(fileName);
    }
}
