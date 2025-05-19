package or.sopt.soptwatcha.service;

import or.sopt.soptwatcha.common.exception.CustomException;
import or.sopt.soptwatcha.common.exception.ErrorCode;
import or.sopt.soptwatcha.domain.*;
import or.sopt.soptwatcha.domain.common.enums.Category;
import or.sopt.soptwatcha.domain.common.enums.IsPositive;
import or.sopt.soptwatcha.domain.common.enums.UpperCategory;
import or.sopt.soptwatcha.repository.CommentKeywordRepository;
import or.sopt.soptwatcha.repository.CommentRepository;
import or.sopt.soptwatcha.repository.MovieKeywordRepository;
import or.sopt.soptwatcha.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    @Mock
    MovieRepository movieRepository;

    @InjectMocks
    MovieServiceImpl movieService;

    @BeforeEach
    void setUp() {
        movieService = Mockito.spy(new MovieServiceImpl(
                commentRepository,
                commentKeywordRepository,
                movieKeywordRepository,
                movieRepository
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


/*    @Test
    @DisplayName("getPreferenceMovies()는 댓글 ID에 따른 영화 추천 리스트를 반환한다")
    void getPreferenceMovies_success() {
        // given
        Long commentId = 1L;

        Comment baseComment = Comment.builder()
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

        CommentKeyword ck1 = CommentKeyword.builder().comment(baseComment).keyword(keyword1).build();
        CommentKeyword ck2 = CommentKeyword.builder().comment(baseComment).keyword(keyword2).build();

        Movie movie1 = Movie.builder().title("영화A").build();
        Comment comment1 = Comment.builder().movie(movie1).build();
        CommentKeyword relatedCK1 = CommentKeyword.builder().comment(comment1).keyword(keyword1).build();

        Movie movie2 = Movie.builder().title("영화B").build();
        Comment comment2 = Comment.builder().movie(movie2).build();
        CommentKeyword relatedCK2 = CommentKeyword.builder().comment(comment2).keyword(keyword2).build();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(baseComment));
        when(commentKeywordRepository.findByComment(baseComment)).thenReturn(List.of(ck1, ck2));
        when(commentKeywordRepository.findByKeyword(keyword1)).thenReturn(List.of(relatedCK1));
        when(commentKeywordRepository.findByKeyword(keyword2)).thenReturn(List.of(relatedCK2));

        // when
        GetPreferenceMoviesListResponse response = movieService.getPreferenceMovies(commentId);

        // then
        assertEquals(2, response.getPreferenceMovies().size());

        KeywordRecommendationGroupResponse group1 = response.getPreferenceMovies().get(0);
        KeywordRecommendationGroupResponse group2 = response.getPreferenceMovies().get(1);

        assertEquals("영화A", group1.getMovies().get(0).getTitle());
        assertEquals("영화B", group2.getMovies().get(0).getTitle());
    }*/


    @Test
    @DisplayName("getPreferenceMovies() 실행 시, 존재하지 않는 commentId로 요청하면 CustomException이 발생한다")
    void getPreferenceMovies_throwsException_whenCommentNotFound() {
        // given
        Long commentId = 999L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            movieService.getPreferenceMovies(commentId);
        });

        assertEquals(ErrorCode.COMMENT_NOT_FOUND, exception.getErrorCode());
    }


    @DisplayName("getMoviesByKeyword()는 키워드에 해당하는 영화들을 반환한다")
    @Test
    void getMoviesByKeyword_returnsMovieList() {
        // given
        Keyword keyword = Keyword.builder().id(1L).value("스릴러").build();

        Movie movie1 = Movie.builder().id(1L).title("기생충").build();
        Movie movie2 = Movie.builder().id(2L).title("올드보이").build();

        Comment comment1 = Comment.builder().id(1L).movie(movie1).build();
        Comment comment2 = Comment.builder().id(2L).movie(movie2).build();

        CommentKeyword ck1 = CommentKeyword.builder().comment(comment1).keyword(keyword).build();
        CommentKeyword ck2 = CommentKeyword.builder().comment(comment2).keyword(keyword).build();

        List<CommentKeyword> commentKeywords = List.of(ck1, ck2);

        when(commentKeywordRepository.findByKeyword(keyword)).thenReturn(commentKeywords);

        // when
        List<Movie> result = movieService.getMoviesByKeyword(keyword);

        // then
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(movie -> movie.getTitle().equals("기생충")));
        assertTrue(result.stream().anyMatch(movie -> movie.getTitle().equals("올드보이")));
    }
}