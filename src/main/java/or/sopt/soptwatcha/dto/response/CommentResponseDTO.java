package or.sopt.soptwatcha.dto.response;

import lombok.Builder;
import lombok.Getter;
import or.sopt.soptwatcha.domain.Comment;

@Getter
@Builder
public class CommentResponseDTO {
    private Long id;
    private double score;
    private String content;
    private int likeCount;
    private int replyCount;

    public static CommentResponseDTO of(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .score(comment.getScore())
                .content(comment.getReview())
                .likeCount(comment.getLikeCount())
                .replyCount(comment.getReplyCount())
                .build();
    }
}