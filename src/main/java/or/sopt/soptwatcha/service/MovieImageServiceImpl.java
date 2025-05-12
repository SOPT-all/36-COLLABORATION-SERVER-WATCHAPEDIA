package or.sopt.soptwatcha.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import or.sopt.soptwatcha.domain.Movie;
import or.sopt.soptwatcha.domain.MovieImage;
import or.sopt.soptwatcha.repository.MovieImageRepository;
import or.sopt.soptwatcha.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MovieImageServiceImpl {

    private final MovieImageRepository movieImageRepository;
    private final MovieRepository movieRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;


    public MovieImage uploadImage(String dirName, MultipartFile file,Long movieId) {

        Movie findMovie = findMovie(movieId);

        String fileName = dirName + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        String originalFileName = file.getOriginalFilename();

        try {
            log.info("이미지 업로드를 시작합니다");

            amazonS3.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata));
            log.info("이미지가 성공적으로 업로드 되었습니다");
        } catch (AmazonServiceException e) {
            throw new IllegalArgumentException("이미지 업로드에 실패하였습니다: " + fileName, e);
        } catch (Exception e) {
            throw new RuntimeException("이미지 업로드에 실패하였습니다: " + e.getMessage(), e);
        }

        String imagePath = amazonS3.getUrl(bucket, fileName).toString();

        MovieImage newImage = MovieImage.builder()
                .imageLink(imagePath)
                .fileName(fileName)
                .imageName(originalFileName)
                .movie(findMovie)
                .build();

        movieImageRepository.save(newImage);
        return newImage;
    }

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
