package or.sopt.soptwatcha.service;

import lombok.RequiredArgsConstructor;
import or.sopt.soptwatcha.domain.Keyword;
import or.sopt.soptwatcha.domain.UpperCategory;
import or.sopt.soptwatcha.domain.common.enums.IsPositive;
import or.sopt.soptwatcha.repository.*;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final CommentRepository commentRepository;
    private final CommentKeywordRepository commentKeywordRepository;
    private final KeywordRepository keywordRepository;
    private final MovieImageRepository movieImageRepository;
    private final MovieKeywordRepository movieKeywordRepository;


    public void getPreferenceMovies(){
        /**
         * 1. 파라미터로 코멘트 식별자를 받아서
         * 2. 해당 코멘트가 존재하는 키워드를 찾는다
         * 3. 그리고 그 키워드들을
         *  3-1. 긍정/부정으로 가르고 부정은 날려버림
         *  3-2. 키워드 별로 우선순위를 책정함
         *
         * 4. 그리고 그 키워드대로 Movie에 어떤게 매핑되어 있는지 찾아보고 상위 네 개 리스트업
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
    }

    public List<Keyword> getPositive(List<Keyword> keywords) {
        return keywords.stream()
                .filter(keyword -> keyword.getIsPositive() == IsPositive.POSITIVE)
                .toList();
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
