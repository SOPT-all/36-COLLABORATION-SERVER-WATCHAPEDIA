package or.sopt.soptwatcha.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import or.sopt.soptwatcha.domain.Movie;
import or.sopt.soptwatcha.domain.MovieImage;
import or.sopt.soptwatcha.dto.response.ImageUploadResponseDTO;
import or.sopt.soptwatcha.repository.MovieImageRepository;
import or.sopt.soptwatcha.repository.MovieRepository;
import or.sopt.soptwatcha.util.S3Util;
import or.sopt.soptwatcha.util.constant.S3FileDirectoryName;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MovieImageServiceImpl {

    private final MovieImageRepository movieImageRepository;
    private final MovieRepository movieRepository;

    private final S3Util s3Util;


    // 영화 포스터 이미지를 업로드 하는 API 입니다
    public String uploadImage(MultipartFile file,Long movieId) {

        String dirName = S3FileDirectoryName.MOVIE_IMAGE_DIRECTORY;
        Movie findMovie = findMovie(movieId);

        ImageUploadResponseDTO upload = s3Util.upload(dirName, file);

        MovieImage newImage = MovieImage.builder()
                .imageLink(upload.getImagePath())
                .fileName(upload.getFilename())
                .imageName(upload.getOriginalFilename())
                .movie(findMovie)
                .build();

        movieImageRepository.save(newImage);
        return newImage.getImageLink();
    }

    /**
     * FIXME
     * 영화 포스터 이미지를 조회 하는 API 입니다
     * -> 이는 추후 영화에서 영화이미지를 조회하여 imageLink를 가져오는 로직으로 리팩터링 됩니다
     */
    public String findByFileName(String fileName) {
        MovieImage byFileName = movieImageRepository.findByFileName(fileName)
                .orElseThrow(()-> new IllegalArgumentException("이미지를 찾지 못했습니다"));

        return byFileName.getImageLink();
    }



    private Movie findMovie(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(()-> new IllegalArgumentException("해당하는 영화를 찾지 못했습니다"));
    }
}
