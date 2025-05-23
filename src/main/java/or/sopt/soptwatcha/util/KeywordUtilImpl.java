package or.sopt.soptwatcha.util;

import or.sopt.soptwatcha.common.exception.CustomException;
import or.sopt.soptwatcha.common.exception.ErrorCode;
import or.sopt.soptwatcha.domain.Keyword;
import or.sopt.soptwatcha.domain.common.enums.IsPositive;
import or.sopt.soptwatcha.domain.common.enums.UpperCategory;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class KeywordUtilImpl implements KeywordUtil {

    @Override
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


    @Override
    public List<Keyword> getPositive(List<Keyword> keywords) {
        List<Keyword> result = keywords.stream()
                .filter(keyword -> keyword.getIsPositive() == IsPositive.POSITIVE)
                .toList();

        if (result.isEmpty()) {
            recommendIfNotExist();
        }

        return result;
    }

    public void recommendIfNotExist(){
        throw new CustomException(ErrorCode.KEYWORD_NOT_FOUND);
    }


    @Override
    public String makeRankingDescription(Keyword keyword) {
        return switch (keyword.getValue()) {
// 구성/스토리
            case "🧠 스토리가 탄탄해요" -> "구성에 빠진 하은님을 위한 작품";
            case "🔀 전개가 흥미진진했어요" -> "흥미진진한 다음 전개가 궁금한 작품들";
            case "💫 설정이 참신했어요" -> "새로움이 가득한 작품이 땡기는 날";
            case "😑 억지스러웠어요" -> "억지스러운 전개가 아쉬운 작품 피하기";
            case "🌀 전개가 엉성해요" -> "전개가 아쉬운 작품 대신 추천";
            case "⛔ 결말이 허무했어요" -> "결말이 인상적인 작품을 찾는다면";

// 연출/스타일
            case "🎬 연출이 새로워요" -> "연출 맛집을 찾는다면";
            case "📸 미장센이 아름다워요" -> "눈과 예술의 작품들";
            case "🎼 음악이 잘 어울려요" -> "귀가 호강하는 작품 추천";
            case "💬 대사가 인상적이에요" -> "인상적인 대사에 꽂힌 하은님께";
            case "🖋️ 작화/영상미가 뛰어나요" -> "뛰어난 영상미로 호평 받은 작품 추천";
            case "🧥 스타일리시해요" -> "스타일리시한 작품을 좋아하는 하은님을 위한 작품";

// 캐릭터/배우
            case "🦹 캐릭터가 매력적이에요" -> "매력적인 캐릭터를 찾는다면";
            case "👥 관계성이 좋아요" -> "인물 간 케미 폭발하는 작품";
            case "👏 연기가 훌륭해요" -> "연기력에 몰입한 하은님을 위한 작품";
            case "🫶 캐릭터에 감정이입 돼요" -> "감정이입 200% 작품";
            case "💔 이해 안 되는 행동이 있어요" -> "납득 가는 캐릭터가 그리울 때";

// 감정/인상
            case "🌼 힐링돼요" -> "하은님의 힐링을 위한 작품";
            case "🥹 감동적이에요" -> "눈물 한 방울의 여운 작품 모음";
            case "😲 반전이 있어요" -> "반전에 놀라고 싶을 때";
            case "😍 설레요" -> "설렘 가득한 이야기";
            case "🤣 웃겨요" -> "웃고 싶을 때 추천";
            case "😭 울컥했어요" -> "울컥주의! 감정폭발";
            case "☺️ 잔잔해요" -> "잔잔한 여운이 남는";
            case "😱 몰입감이 최고예요" -> "정주행 각, 몰입 100%";
            case "🤯 생각이 많아졌어요" -> "여운과 질문을 주는";
            case "🫠 지루했어요" -> "지루함 없이 즐길 수 있는 추천작";

// 추천 대상
            case "🙋🏻‍♀️ 혼자" -> "혼자만의 시간을 위한 추천작";
            case "👯 친구랑" -> "친구와 함께 보면 좋은";
            case "👨‍👩‍👧 가족이랑" -> "가족과 보기 좋은 이야기";
            case "👩‍❤️‍👨 연인이랑" -> "연인과 함께라면 더 좋아";

            default -> "추천 작품 모음";
        };
    }
}
