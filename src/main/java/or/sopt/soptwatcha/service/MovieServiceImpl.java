package or.sopt.soptwatcha.service;

import lombok.RequiredArgsConstructor;
import or.sopt.soptwatcha.common.exception.CustomException;
import or.sopt.soptwatcha.common.exception.ErrorCode;
import or.sopt.soptwatcha.domain.*;
import or.sopt.soptwatcha.domain.common.enums.IsPositive;
import or.sopt.soptwatcha.domain.common.enums.MovieImageType;
import or.sopt.soptwatcha.dto.response.GetPreferenceMoviesListResponse;
import or.sopt.soptwatcha.dto.response.GetPreferenceMoviesResponse;
import or.sopt.soptwatcha.dto.response.KeywordRecommendationGroupResponse;
import or.sopt.soptwatcha.dto.response.KeywordResponseDTO;
import or.sopt.soptwatcha.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final CommentRepository commentRepository;
    private final CommentKeywordRepository commentKeywordRepository;
    private final MovieKeywordRepository movieKeywordRepository;


    /**
     * 1. 파라미터로 코멘트 식별자를 받아서
     * 2. 해당 코멘트가 존재하는 키워드를 찾는다
     * 3. 그리고 그 키워드들을
     *  3-1. 긍정/부정으로 가르고 부정은 날려버림
     *  3-2. 키워드 별로 우선순위를 책정함
     *
     * 아 이렇게 하면 안되는 것 같은데
     * 이렇게 하는게 아니라 해당 키워드를 매핑하고 있는 코멘트를 전부 찾아서
     * 그 코멘트가 달린 영화를 모두 찾아서
     * 그 영화 중 다섯개를 리스트업 해야함
     *
     * 4. 그리고 그 키워드대로 Movie에 어떤게 매핑되어 있는지 찾아보고 상위 다섯개 리스트업 -> X
     *  4-1. 이때 DTO 하나 쓰고
     * 5. 그리고 그걸 전부 감싸서 반환
     *  5-1. 이때 DTO 하나 더
     *
     * 그럼 필요한 repository가
     *  1. commentRepository
     *  2. commentKeywordRepository
     *  3. keywordRepository
     *  4. movieRepository
     *  5. movieImageRepository
     *  6. movieKeywordRepsoitory
     * */
    @Override
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
