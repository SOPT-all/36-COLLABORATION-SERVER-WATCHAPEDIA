package or.sopt.soptwatcha.service;

import or.sopt.soptwatcha.domain.Keyword;
import or.sopt.soptwatcha.domain.UpperCategory;
import or.sopt.soptwatcha.domain.common.enums.Category;
import or.sopt.soptwatcha.domain.common.enums.IsPositive;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @InjectMocks
    MovieServiceImpl movieService;

    @Test
    @DisplayName("getPriorityOrderedKeywords()을 이용하여 키워드를 우선순위대로 재배치 할 수 있다")
    void getPriorityOrderedKeywords_success() {
        // given

        Keyword k1 = Keyword.builder()
                .value("와 대박")
                .upperCategory(UpperCategory.EMOTION_IMPRESSION)
                .isPositive(IsPositive.POSITIVE)
                .category(Category.MOVIE_KEYWORD)
                .build();

        Keyword k2 = Keyword.builder()
                .value("진짜 대박이다")
                .upperCategory(UpperCategory.CHARACTER_ACTOR)
                .isPositive(IsPositive.POSITIVE)
                .category(Category.MOVIE_KEYWORD)
                .build();

        Keyword k3 = Keyword.builder()
                .value("레전드네")
                .upperCategory(UpperCategory.DIRECTION_STYLE)
                .isPositive(IsPositive.POSITIVE)
                .category(Category.MOVIE_KEYWORD)
                .build();

        List<Keyword> input = List.of(k2, k3, k1);

        // when
        List<Keyword> result = movieService.getPriorityOrderedKeywords(input);

        // then
        assertEquals(k1, result.get(0));
        assertEquals(k3, result.get(1));
        assertEquals(k2, result.get(2));
    }


    @Test
    @DisplayName("getPositive()를 이용하여 긍정 키워드만 반환 할 수 있다")
    void getPositive() {
        // given
        Keyword positiveKeyword = Keyword.builder()
                .value("말도 안된다")
                .isPositive(IsPositive.POSITIVE)
                .upperCategory(UpperCategory.EMOTION_IMPRESSION)
                .category(Category.MOVIE_KEYWORD)
                .build();

        Keyword negativeKeyword = Keyword.builder()
                .value("진짜 말도 안돼")
                .isPositive(IsPositive.NEGATIVE)
                .upperCategory(UpperCategory.EMOTION_IMPRESSION)
                .category(Category.MOVIE_KEYWORD)
                .build();

        List<Keyword> input = List.of(positiveKeyword, negativeKeyword);

        // when
        List<Keyword> result = movieService.getPositive(input);

        // then
        assertEquals(1, result.size());
        assertTrue(result.contains(positiveKeyword));
        assertFalse(result.contains(negativeKeyword));
    }
}