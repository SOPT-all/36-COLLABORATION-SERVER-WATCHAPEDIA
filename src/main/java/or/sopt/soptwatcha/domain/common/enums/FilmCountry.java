package or.sopt.soptwatcha.domain.common.enums;

import lombok.Getter;

@Getter
public enum FilmCountry {
    KOR("한국"),
    USA("미국"),
    GBR("영국"),
    FRA("프랑스"),
    JPN("일본"),
    CHN("중국");

    private final String koreanName;

    FilmCountry(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getDisplayName() {
        return this.koreanName;
    }
}
