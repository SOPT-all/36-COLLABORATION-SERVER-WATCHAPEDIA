package or.sopt.soptwatcha.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import or.sopt.soptwatcha.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentCreateResponseDTO {
    private Long id;
    private Long movieId;
    private String review;
    private LocalDateTime createdAt;
    private List<String> keywords;
    
    public static CommentCreateResponseDTO of(Comment comment, List<String> keywordValues) {
        return CommentCreateResponseDTO.builder()
                .id(comment.getId())
                .movieId(comment.getMovie().getId())
                .review(comment.getReview())
                .createdAt(comment.getCreatedAt())
                .keywords(keywordValues)
                .build();
    }
}