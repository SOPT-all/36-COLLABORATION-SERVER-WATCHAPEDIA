package or.sopt.soptwatcha.domain.common.enums;

import lombok.Getter;

@Getter
public enum UpperCategory {

    CONSTRUCTOR_STORY("구성/스토리"),
    DIRECTION_STYLE("연출/스타일"),
    CHARACTER_ACTOR("캐릭터/배우"),
    EMOTION_IMPRESSION("감정/인상"),
    RECOMMENDED_FOR("추천 대상");

    private final String description;

    UpperCategory(String description) {
        this.description = description;
    }
}