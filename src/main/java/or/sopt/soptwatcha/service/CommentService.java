package or.sopt.soptwatcha.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import or.sopt.soptwatcha.common.exception.ErrorCode;
import or.sopt.soptwatcha.common.exception.CustomException;
import or.sopt.soptwatcha.domain.Comment;
import or.sopt.soptwatcha.domain.CommentKeyword;
import or.sopt.soptwatcha.domain.Keyword;
import or.sopt.soptwatcha.domain.Movie;
import or.sopt.soptwatcha.domain.common.enums.Category;
import or.sopt.soptwatcha.dto.request.CommentCreateRequestDTO;
import or.sopt.soptwatcha.dto.response.CommentCreateResponseDTO;
import or.sopt.soptwatcha.repository.CommentKeywordRepository;
import or.sopt.soptwatcha.repository.CommentRepository;
import or.sopt.soptwatcha.repository.KeywordRepository;
import or.sopt.soptwatcha.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final MovieRepository movieRepository;
    private final KeywordRepository keywordRepository;
    private final CommentKeywordRepository commentKeywordRepository;

    @Transactional
    public CommentCreateResponseDTO createComment(Long postId, CommentCreateRequestDTO request) {
        Movie movie = movieRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        List<Keyword> keywords = validateAndGetKeywords(request.getKeywordIds());

        List<CommentKeyword> commentKeywords = keywords.stream()
                .map(CommentKeyword::createCommentKeyword)
                .collect(Collectors.toList());

        Comment comment = Comment.createCommentWithKeywords(movie, request.getReview(), commentKeywords);

        Comment savedComment = commentRepository.save(comment);

        List<String> keywordValues = keywords.stream()
                .map(Keyword::getValue)
                .collect(Collectors.toList());

        return CommentCreateResponseDTO.of(savedComment, keywordValues);
    }

    private List<Keyword> validateAndGetKeywords(List<Long> keywordIds) {
        List<Keyword> keywords = keywordRepository.findAllById(keywordIds);
        
        if (keywords.size() != keywordIds.size()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "존재하지 않는 키워드가 포함되어 있습니다.");
        }
        
        boolean allValidKeywords = keywords.stream()
                .allMatch(keyword -> keyword.getCategory() == Category.COMMENT_KEYWORD);
                
        if (!allValidKeywords) {
            throw new CustomException(ErrorCode.INVALID_KEYWORD_CATEGORY);
        }
        
        if (keywords.size() < 1 || keywords.size() > 5) {
            throw new CustomException(ErrorCode.INVALID_KEYWORD_COUNT);
        }
        
        return keywords;
    }
}