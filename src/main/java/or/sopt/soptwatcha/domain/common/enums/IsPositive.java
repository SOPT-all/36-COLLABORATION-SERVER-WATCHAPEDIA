package or.sopt.soptwatcha.domain.common.enums;

import lombok.Getter;

@Getter
public enum IsPositive {

    POSITIVE("긍정태그"),
    NEGATIVE("부정태그"),
    NONE("해당없음");

    private final String description;

    IsPositive(String description) {
        this.description = description;
    }

}