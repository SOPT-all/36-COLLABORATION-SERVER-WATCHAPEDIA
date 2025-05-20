package or.sopt.soptwatcha.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class CommentKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;

    public void setComment(Comment comment) {
        this.comment = comment;
        if (comment != null && !comment.getCommentKeywords().contains(this)) {
            comment.addCommentKeyword(this);
        }
    }

    public static CommentKeyword createCommentKeyword(Keyword keyword) {
        return CommentKeyword.builder()
                .keyword(keyword)
                .build();
    }
}
