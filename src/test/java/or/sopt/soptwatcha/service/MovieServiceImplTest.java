package or.sopt.soptwatcha.service;

import or.sopt.soptwatcha.domain.*;
import or.sopt.soptwatcha.domain.common.enums.Category;
import or.sopt.soptwatcha.domain.common.enums.IsPositive;
import or.sopt.soptwatcha.dto.response.GetPreferenceMoviesListResponse;
import or.sopt.soptwatcha.dto.response.GetPreferenceMoviesResponse;
import or.sopt.soptwatcha.repository.CommentKeywordRepository;
import or.sopt.soptwatcha.repository.CommentRepository;
import or.sopt.soptwatcha.repository.MovieKeywordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {



    @Mock
    CommentRepository commentRepository;

    @Mock
    CommentKeywordRepository commentKeywordRepository;

    @Mock
    MovieKeywordRepository movieKeywordRepository;

    @InjectMocks
    MovieServiceImpl movieService;

    @BeforeEach
    void setUp() {
        movieService = Mockito.spy(new MovieServiceImpl(
                commentRepository,
                commentKeywordRepository,
                movieKeywordRepository
        ));
    }

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


    @Test
    @DisplayName("getPreferenceMoviesResponses()는 키워드에 매핑된 영화들을 반환한다")
    void getPreferenceMoviesResponses_success() {
        // given
        Keyword keyword = Keyword.builder()
                .value("레전드")
                .isPositive(IsPositive.POSITIVE)
                .upperCategory(UpperCategory.DIRECTION_STYLE)
                .category(Category.MOVIE_KEYWORD)
                .build();

        Movie movie1 = Movie.builder()
                .title("진짜 합세 짱이다")
                .build();

        Movie movie2 = Movie.builder()
                .title("진짜 대박이야")
                .build();

        MovieKeyword mk1 = MovieKeyword.builder()
                .keyword(keyword)
                .movie(movie1)
                .build();

        MovieKeyword mk2 = MovieKeyword.builder()
                .keyword(keyword)
                .movie(movie2)
                .build();

        List<MovieKeyword> mockResult = List.of(mk1, mk2);

        // mock 설정
        when(movieKeywordRepository.findByKeyword(keyword)).thenReturn(mockResult);

        // when
        List<GetPreferenceMoviesResponse> responses = movieService.getPreferenceMoviesResponses(keyword);

        // then
        assertEquals(2, responses.size());
        assertEquals("진짜 합세 짱이다", responses.get(0).getTitle());
        assertEquals("진짜 대박이야", responses.get(1).getTitle());
    }


    @Test
    @DisplayName("getPreferenceMovies()는 댓글 ID에 따른 영화 추천 리스트를 반환한다")
    void getPreferenceMovies_success() {
        // given
        Long commentId = 1L;

        Comment comment = Comment.builder()
                .id(commentId)
                .review("이 영화 진짜 대박")
                .build();

        Keyword keyword1 = Keyword.builder()
                .value("연출 미쳤다")
                .isPositive(IsPositive.POSITIVE)
                .upperCategory(UpperCategory.DIRECTION_STYLE)
                .category(Category.MOVIE_KEYWORD)
                .build();

        Keyword keyword2 = Keyword.builder()
                .value("배우 몰입감")
                .isPositive(IsPositive.POSITIVE)
                .upperCategory(UpperCategory.CHARACTER_ACTOR)
                .category(Category.MOVIE_KEYWORD)
                .build();

        CommentKeyword ck1 = CommentKeyword.builder()
                .comment(comment)
                .keyword(keyword1)
                .build();

        CommentKeyword ck2 = CommentKeyword.builder()
                .comment(comment)
                .keyword(keyword2)
                .build();

        Movie movie1 = Movie.builder().title("영화A").build();
        Movie movie2 = Movie.builder().title("영화B").build();

        MovieKeyword mk1 = MovieKeyword.builder().movie(movie1).keyword(keyword1).build();
        MovieKeyword mk2 = MovieKeyword.builder().movie(movie2).keyword(keyword2).build();

        // mock 설정
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentKeywordRepository.findByComment(comment)).thenReturn(List.of(ck1, ck2));
        when(movieKeywordRepository.findByKeyword(keyword1)).thenReturn(List.of(mk1));
        when(movieKeywordRepository.findByKeyword(keyword2)).thenReturn(List.of(mk2));

        // when
        GetPreferenceMoviesListResponse response = movieService.getPreferenceMovies(commentId);

        // then
        assertEquals(2, response.getResult().size());

        List<GetPreferenceMoviesResponse> group1 = response.getResult().get(0);
        List<GetPreferenceMoviesResponse> group2 = response.getResult().get(1);

        assertEquals("영화A", group1.get(0).getTitle());
        assertEquals("영화B", group2.get(0).getTitle());
    }


    @Test
    @DisplayName("getPreferenceMovies() 실행 시, 긍정 키워드가 없으면 recommendIfNotExist가 호출된다")
    void recommendIfNotExist_is_called_when_no_positive_keywords() {
        // given
        Long commentId = 1L;

        Comment mockComment = Comment.builder().build();
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        Keyword negativeKeyword = Keyword.builder()
                .isPositive(IsPositive.NEGATIVE)
                .build();
        CommentKeyword commentKeyword = CommentKeyword.builder()
                .keyword(negativeKeyword)
                .build();
        when(commentKeywordRepository.findByComment(mockComment)).thenReturn(List.of(commentKeyword));

        doNothing().when(movieService).recommendIfNotExist();

        // when
        movieService.getPreferenceMovies(commentId);

        // then
        verify(movieService, times(1)).recommendIfNotExist();
    }


    @Test
    @DisplayName("getPreferenceMovies() 실행 시, 존재하지 않는 commentId로 요청하면 예외가 발생한다")
    void getPreferenceMovies_throwsException_whenCommentNotFound() {
        // given
        Long commentId = 999L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            movieService.getPreferenceMovies(commentId);
        });

        assertEquals("해당하는 코멘트를 찾을 수 없습니다", exception.getMessage());
    }
}