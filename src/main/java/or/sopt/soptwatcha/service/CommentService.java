package or.sopt.soptwatcha.service;

import lombok.RequiredArgsConstructor;
import or.sopt.soptwatcha.common.exception.ErrorCode;
import or.sopt.soptwatcha.common.exception.CustomException;
import or.sopt.soptwatcha.domain.Comment;
import or.sopt.soptwatcha.dto.response.CommentResponseDTO;
import or.sopt.soptwatcha.repository.CommentRepository;
import or.sopt.soptwatcha.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MovieRepository movieRepository;

    @Transactional(readOnly = true)
    public CommentResponseDTO getTopLikedComment(Long movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new CustomException(ErrorCode.MOVIE_NOT_FOUND);
        }

        Comment comment = commentRepository.findTopByMovieIdOrderByLikeCountDesc(movieId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        return CommentResponseDTO.of(comment);
    }
}