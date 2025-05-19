package or.sopt.soptwatcha.service;

import lombok.RequiredArgsConstructor;
import or.sopt.soptwatcha.common.exception.CustomException;
import or.sopt.soptwatcha.common.exception.ErrorCode;
import or.sopt.soptwatcha.domain.*;
import or.sopt.soptwatcha.domain.common.enums.Category;
import or.sopt.soptwatcha.domain.common.enums.IsPositive;
import or.sopt.soptwatcha.domain.common.enums.MovieImageType;
import or.sopt.soptwatcha.domain.common.enums.UpperCategory;
import or.sopt.soptwatcha.dto.response.*;
import or.sopt.soptwatcha.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final CommentRepository commentRepository;
    private final CommentKeywordRepository commentKeywordRepository;
    private final MovieRepository movieRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final MovieArtistRepository movieArtistRepository;
    private final MovieImageRepository movieImageRepository;

    
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
    public GetPreferenceMoviesListResponse getPreferenceMovies(Long commentId) {

        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        List<CommentKeyword> byComment = commentKeywordRepository.findByComment(findComment);

        List<Keyword> keywords = byComment.stream()
                .map(CommentKeyword::getKeyword)
                .toList();

        List<Keyword> positiveKeywords = getPositive(keywords);
        if (positiveKeywords.isEmpty()) {
            recommendIfNotExist();
        }

        List<Keyword> priorityOrderedKeywords = getPriorityOrderedKeywords(positiveKeywords);

        List<KeywordRecommendationGroupResponse> groupedResult = new ArrayList<>();

        for (Keyword keyword : priorityOrderedKeywords) {
            List<Movie> movieList = getMoviesByKeyword(keyword);

            List<GetPreferenceMoviesResponse> dtoList = movieList.stream()
                    .map(movie -> {
                        MovieImage mainImage = movie.getMovieImages().stream()
                                .filter(img -> img.getMovieImageType() == MovieImageType.POSTER)
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("영화에 해당하는 이미지가 존재하지 않습니다"));

                        List<KeywordResponseDTO> movieKeywords = movie.getMovieKeywords().stream()
                                .map(MovieKeyword::getKeyword)
                                .map(KeywordResponseDTO::of)
                                .toList();

                        return GetPreferenceMoviesResponse.of(movie, mainImage, movieKeywords);
                    })
                    .toList();

            String description = makeRankingDescription(keyword); // 설명 필드 생성

            groupedResult.add(
                    KeywordRecommendationGroupResponse.builder()
                            .description(description)
                            .movies(dtoList)
                            .build()
            );
        }

        return GetPreferenceMoviesListResponse.of(groupedResult);
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

        // 0. 무비 타입에 따른 영화 조회
        List<Movie> top5ByClosestToDate = movieRepository.findTop5ByClosestToDateAndMovieType(now,movieType);

        List<GetMovieSoonResponseDTO> responseList = top5ByClosestToDate.stream()
                .map(movie -> {
                    // 1. 포스터 이미지 경로 추출
                    String posterLink = movie.getMovieImages().stream()
                            .filter(img -> img.getMovieImageType() == MovieImageType.POSTER)
                            .findFirst()
                            .map(MovieImage::getImageLink)
                            .orElse(null);

                    // 2. 개봉일까지 남은 일 수 계산
                    int untilRelease = (int) ChronoUnit.DAYS.between(now, movie.getReleaseYear());

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

    public List<Movie> getMoviesByKeyword(Keyword keyword) {

        // 우선순위 정렬된 키워드를 대상으로 해당 키워드를 갖고 있는 키워드 코멘트들을 찾는다 -> List<KeywordComment> 가 반환될거임
        List<CommentKeyword> byKeyword = commentKeywordRepository.findByKeyword(keyword);

        // 그리고 그 키워드 코멘트들의 코멘트를 찾는다 -> List<Comment>
        List<Comment> comments = byKeyword.stream()
                .map(CommentKeyword::getComment)
                .toList();

        // 그리고 그 코멘트들이 평가한 영화를 찾는다 -> List<Movie>
        return comments.stream()
                .map(Comment::getMovie)
                .distinct()
                .toList();
    }


    public List<Keyword> getPositive(List<Keyword> keywords) {
        return keywords.stream()
                .filter(keyword -> keyword.getIsPositive() == IsPositive.POSITIVE)
                .toList();
    }


    public void recommendIfNotExist(){
        throw new IllegalArgumentException("긍정 키워드가 존재하지 않습니다. 추후 로직으로 업데이트 될 예정입니다");
    }


    public List<Keyword> getPriorityOrderedKeywords(List<Keyword> keywords) {
        // 1. 우선순위 기준 맵 정의
        Map<UpperCategory, Integer> priorityMap = Map.of(
                UpperCategory.EMOTION_IMPRESSION, 1,
                UpperCategory.RECOMMENDED_FOR, 2,
                UpperCategory.DIRECTION_STYLE, 3,
                UpperCategory.CONSTRUCTOR_STORY, 4,
                UpperCategory.CHARACTER_ACTOR, 5
        );

        // 2. 정렬 수행 (우선순위 오름차순)
        return keywords.stream()
                .sorted(Comparator.comparingInt(k ->
                        priorityMap.getOrDefault(k.getUpperCategory(), Integer.MAX_VALUE)))
                .collect(Collectors.toList());
    }


    private String makeRankingDescription(Keyword keyword) {
        return switch (keyword.getValue()) {
            // 구성/스토리
            case "스토리가 탄탄해" -> "구성에 빠진 00님을 위한 작품";
            case "전개가 흥미진진했어요" -> "흥미진진한 다음 전개가 궁금한 작품들";
            case "설정이 참신했어요" -> "새로움이 가득한 작품이 땡기는 날";

            // 연출/스타일
            case "연출이 훌륭해요" -> "연출 맛집을 찾는다면";
            case "미장센이 아름다워요" -> "눈과 예술의 작품들";
            case "음악이 좋았어요" -> "귀가 호강하는 작품 추천";
            case "대사가 인상적이에요" -> "인상적인 대사에 꽂힌 00님께";
            case "작화/영상미가 뛰어나요" -> "뛰어난 영상미로 호평 받은 작품 추천";
            case "스타일리시해요" -> "스타일리시한 작품을 좋아하는 00님을 위한 작품";

            // 캐릭터/배우
            case "캐릭터가 매력적이에요" -> "매력적인 캐릭터를 찾는다면";
            case "관계성이 좋아요" -> "인물 간 케미 폭발하는 작품";
            case "연기력이 훌륭해요" -> "연기력에 몰입한 00님을 위한 작품";
            case "캐릭터에 감정이입 했어요" -> "감정이입 200% 작품";

            // 감정/인상
            case "힐링돼요" -> "00님의 힐링을 위한 작품";
            case "감동적이에요" -> "눈물 한 방울의 여운 작품 모음";
            case "반전이 있어요" -> "반전에 놀라고 싶을 때";
            case "설레요" -> "설렘 가득한 이야기";
            case "웃겨요" -> "웃고 싶을 때 추천";
            case "울컥했어요" -> "울컥주의! 감정폭발";
            case "잔잔해요" -> "잔잔한 여운이 남는";
            case "몰입감이 최고예요" -> "정주행 각, 몰입 100%";
            case "생각이 많아졌어요" -> "여운과 질문을 주는";

            // 추천 대상
            case "혼자" -> "혼자만의 시간을 위한 추천작";
            case "친구랑" -> "친구와 함께 보면 좋은";
            case "가족이랑" -> "가족과 보기 좋은 이야기";
            case "연인이랑" -> "연인과 함께라면 더 좋아";

            default -> "추천 작품 모음";
        };
    }
}
