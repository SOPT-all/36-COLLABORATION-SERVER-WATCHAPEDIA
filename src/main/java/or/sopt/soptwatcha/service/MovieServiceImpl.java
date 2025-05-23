package or.sopt.soptwatcha.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import or.sopt.soptwatcha.common.exception.CustomException;
import or.sopt.soptwatcha.common.exception.ErrorCode;
import or.sopt.soptwatcha.domain.*;
import or.sopt.soptwatcha.domain.common.enums.Category;
import or.sopt.soptwatcha.domain.common.enums.MovieImageType;
import or.sopt.soptwatcha.domain.common.enums.MovieType;
import or.sopt.soptwatcha.dto.response.*;
import or.sopt.soptwatcha.repository.*;
import or.sopt.soptwatcha.util.KeywordUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieServiceImpl implements MovieService {

    private final CommentRepository commentRepository;
    private final CommentKeywordRepository commentKeywordRepository;
    private final MovieRepository movieRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final MovieArtistRepository movieArtistRepository;
    private final MovieImageRepository movieImageRepository;

    private final KeywordUtil keywordUtil;



    @Override
    @Transactional(readOnly = true)
    public MovieDetailResponseDTO getMovieDetail(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REQUEST, "해당 영화를 찾을 수 없습니다."));

        List<MovieGenre> movieGenres = movieGenreRepository.findAllByMovie(movie);
        List<MovieArtist> movieArtists = movieArtistRepository.findAllByMovie(movie);
        List<MovieImage> movieImages = movieImageRepository.findAllByMovie(movie);

        return MovieDetailResponseDTO.from(movie, movieGenres, movieArtists, movieImages);
    }


    @Override
    @Transactional(readOnly = true)
    public GetPreferenceMoviesResponse.GetPreferenceMoviesResponseWrapper getPreferenceMoviesV2(Long commentId) {

        // 댓글을 찾는다
        List<Keyword> keywords = getKeywordsByComment(commentId);

        // 댓글의 키워드 중 긍정과 부정을 필터링 한다
        List<Keyword> positiveKeywords = keywordUtil.getPositive(keywords);

        // 긍정의 키워드들에서 우선순위대로 재정렬한다 (이때 우선순위는 상위 키워드로 진행한다)
        List<Keyword> priorityOrderedKeywords = keywordUtil.getPriorityOrderedKeywords(positiveKeywords);

        List<GetPreferenceMoviesResponse.GetPreferenceMoviesGroupDTO> grouped = priorityOrderedKeywords.stream()
                .map(keyword -> {

                    // 키워드로 매핑테이블들을 찾는다: 이때부터 이중 리스트 시작
                    List<CommentKeyword> ckList = commentKeywordRepository.findByKeyword_Id(keyword.getId());

                    List<GetPreferenceMoviesResponse> movies = ckList.stream()
                            // 매핑테이블로 댓글을 찾고 댓글로 영화를 찾는다
                            .map(ck -> ck.getComment().getMovie())
                            .distinct()
                            .limit(5)
                            .map(movie -> {
                                // 각 영화에서 포스터를 찾는다
                                MovieImage posterImage = getMoviePoster(movie);
                                // 영화에서 매핑테이블 -> 키워드로 영화 키워드도 찾는다
                                List<GetPreferenceMoviesResponse.GetPreferenceMoviesKeywordResponseDTO> keywordDTOs = getGetPreferenceMoviesKeywordResponseDTOS(movie);
                                // 그리고 그걸 다 묶어서 하나의 영화를 위한 DTO로 묶는다
                                return GetPreferenceMoviesResponse.of(movie, posterImage, keywordDTOs);
                            })
                            .toList();

                    // description은 키워드 하위 카테고리 기반 예시 생성
                    String description = keywordUtil.makeRankingDescription(keyword);

                    // description을 포함한 DTO로 정리하고 그것까지 포함한 리스트로 반환한다
                    return GetPreferenceMoviesResponse.GetPreferenceMoviesGroupDTO.of(description,movies,keyword);
                })
                .toList();

        // 지금까지 리스트였으므로 리스트를 포함한 하나의 DTO로 최종적으로 반환한다
        return GetPreferenceMoviesResponse.GetPreferenceMoviesResponseWrapper.builder()
                .preferenceMovies(grouped)
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public GetMovieTopRankingResponseDTO.GetMovieTopRankingResponseListDTO getMovieTopRanking() {
        List<Movie> top5MoviesByExpectScore = movieRepository.findTop5ByOrderByScoreDesc();

        List<GetMovieTopRankingResponseDTO> responseList = top5MoviesByExpectScore.stream()
                .map(movie -> {
                    // 1. 포스터 이미지 경로 추출
                    String posterLink = movie.getMovieImages().stream()
                            .filter(img -> img.getMovieImageType() == MovieImageType.POSTER)
                            .findFirst()
                            .map(MovieImage::getImageLink)
                            .orElse(null);

                    // 2. MOVIE_KEYWORD에 해당하는 키워드 리스트 추출
                    List<String> movieKeywordValues = movie.getMovieKeywords().stream()
                            .map(MovieKeyword::getKeyword)
                            .filter(keyword -> keyword.getCategory() == Category.MOVIE_KEYWORD)
                            .map(Keyword::getValue)
                            .toList();

                    // 3. DTO로 변환
                    return GetMovieTopRankingResponseDTO.from(movie, posterLink, movieKeywordValues);
                })
                .toList();

        // 4. 최종 DTO로 한 번 더 감싸기
        return GetMovieTopRankingResponseDTO.GetMovieTopRankingResponseListDTO.builder()
                .result(responseList)
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public GetMovieSoonResponseDTO.GetMovieSoonResponseListDTO getSoon(String movieType) {

        LocalDate now = LocalDate.now();

        MovieType movieType1 = MovieType.valueOf(movieType);

        // 0. 무비 타입에 따른 영화 조회
        List<Movie> top5ByClosestToDate = movieRepository.findTop5ByMovieTypeAndUploadYearGreaterThanEqualOrderByUploadYearAsc(movieType1,now);

        List<GetMovieSoonResponseDTO> responseList = top5ByClosestToDate.stream()
                .map(movie -> {
                    // 1. 포스터 이미지 경로 추출
                    String posterLink = movie.getMovieImages().stream()
                            .filter(img -> img.getMovieImageType() == MovieImageType.POSTER)
                            .findFirst()
                            .map(MovieImage::getImageLink)
                            .orElse(null);

                    // 2. 개봉일까지 남은 일 수 계산
                    int untilRelease = (int) ChronoUnit.DAYS.between(now, movie.getUploadYear());

                    // 3. DTO 변환
                    return GetMovieSoonResponseDTO.from(movie, posterLink, untilRelease);
                })
                .toList();

        // 4. 리스트 DTO로 감싸서 반환
        return GetMovieSoonResponseDTO.GetMovieSoonResponseListDTO.builder()
                .soons(responseList)
                .build();
    }





    //-------------helper method----------------------------------------------------------//

    // 영화를 받으면 영화 포스터를 반환하는 헬퍼 메서드
    private static MovieImage getMoviePoster(Movie movie) {

        return movie.getMovieImages().stream()
                .filter(img -> img.getMovieImageType() == MovieImageType.POSTER)
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_POSTER_NOT_FOUND));
    }


    // movie를 받으면 영화의 키워드를 반환하여 value만 담은 DTO로 감싸는 메서드
    private static List<GetPreferenceMoviesResponse.GetPreferenceMoviesKeywordResponseDTO> getGetPreferenceMoviesKeywordResponseDTOS(Movie movie) {
        return movie.getMovieKeywords().stream()
                .map(MovieKeyword::getKeyword)
                .filter(k -> k.getCategory() == Category.MOVIE_KEYWORD)
                .map(GetPreferenceMoviesResponse.GetPreferenceMoviesKeywordResponseDTO::of)
                .toList();
    }


    // 댓글을 받으면 해당하는 키워드를 반환하는 메서드
    private List<Keyword> getKeywordsByComment(Long commentId) {
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        List<CommentKeyword> byComment = commentKeywordRepository.findByComment(findComment);

        return byComment.stream()
                .map(CommentKeyword::getKeyword)
                .toList();
    }
}
