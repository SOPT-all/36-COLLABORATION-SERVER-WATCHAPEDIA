package or.sopt.soptwatcha.domain.common.enums;

import lombok.Getter;

@Getter
public enum Category {
    MOVIE_KEYWORD("영화에 사용되는 키워드"),
    COMMENT_KEYWORD("댓글에 사용되는 키워드");

    private final String description;

    Category(String description) {
        this.description = description;
    }

    }
