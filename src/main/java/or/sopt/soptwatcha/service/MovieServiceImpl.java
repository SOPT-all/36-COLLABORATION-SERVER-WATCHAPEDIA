package or.sopt.soptwatcha.service;

import lombok.RequiredArgsConstructor;
import or.sopt.soptwatcha.common.exception.CustomException;
import or.sopt.soptwatcha.common.exception.ErrorCode;
import or.sopt.soptwatcha.domain.*;
import or.sopt.soptwatcha.domain.common.enums.IsPositive;
import or.sopt.soptwatcha.dto.response.GetPreferenceMoviesListResponse;
import or.sopt.soptwatcha.dto.response.GetPreferenceMoviesResponse;
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

        // 댓글을 찾고
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // 그 댓글과 키워드의 매핑 테이블들을 찾는다 (키워드가 여러개일 수 있어서 List)
        List<CommentKeyword> byComment = commentKeywordRepository.findByComment(findComment);

        // 그리고 거기서 키워드를 추출하고
        List<Keyword> keywords = byComment.stream()
                .map(CommentKeyword::getKeyword)
                .toList();

        // 긍정 키워드를 추출한다 -> 만약 긍정 키워드가 존재하지 않으면 recommendIfNotExist 로직 실행
        List<Keyword> positiveKeywords = getPositive(keywords);
        if (positiveKeywords.isEmpty()) {
            recommendIfNotExist();
        }

        // 추출된 긍정 키워드들을 우선순위대로 재배치하고
        List<Keyword> priorityOrderedKeywords = getPriorityOrderedKeywords(positiveKeywords);

        // 같은 키워드로 평가한 댓글을 찾고 그 댓글들이 평가한 영화들을 찾는다 -> 그 영화들을 List<ResponseDTO>로 묶어서 2차원 리스트로 반환
        List<List<GetPreferenceMoviesResponse>> result = priorityOrderedKeywords.stream()
                .map(this::getMoviesByKeyword)
                .toList();

        // 그걸 DTO 에 담아서 최종 반환한다
        return GetPreferenceMoviesListResponse.of(result);
    }














    //-------------helper method----------------------------------------------------------//

    public List<GetPreferenceMoviesResponse> getMoviesByKeyword(Keyword keyword) {

        // 우선순위 정렬된 키워드를 대상으로 해당 키워드를 갖고 있는 키워드 코멘트들을 찾는다 -> List<KeywordComment> 가 반환될거임
        List<CommentKeyword> byKeyword = commentKeywordRepository.findByKeyword(keyword);

        // 그리고 그 키워드 코멘트들의 코멘트를 찾는다 -> List<Comment>
        List<Comment> comments = byKeyword.stream()
                .map(CommentKeyword::getComment)
                .toList();

        // 그리고 그 코멘트들이 평가한 영화를 찾는다 -> List<Movie>
        List<Movie> movies = comments.stream()
                .map(Comment::getMovie)
                .distinct()
                .toList();

        // DTO로 감싼다
        return movies.stream()
                .map(GetPreferenceMoviesResponse::of)
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
}
