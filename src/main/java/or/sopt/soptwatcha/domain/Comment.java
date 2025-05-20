package or.sopt.soptwatcha.domain;

import jakarta.persistence.*;
import lombok.*;
import or.sopt.soptwatcha.domain.common.BaseEntity;
import or.sopt.soptwatcha.domain.common.enums.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String review;

    private int score;

    private int likeCount;

    private int replyCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Builder.Default
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentKeyword> commentKeywords = new ArrayList<>();

    public List<CommentKeyword> getCommentKeywords() {
        return Collections.unmodifiableList(commentKeywords);
    }

    protected void addCommentKeyword(CommentKeyword commentKeyword) {
        if (this.commentKeywords == null) {
            this.commentKeywords = new ArrayList<>();
        }
        if (!this.commentKeywords.contains(commentKeyword)) {
            this.commentKeywords.add(commentKeyword);
            if (commentKeyword.getComment() != this) {
                commentKeyword.setComment(this);
            }
        }
    }

    public static Comment createCommentWithKeywords(Movie movie, String review, List<CommentKeyword> keywords) {
        Comment comment = Comment.builder()
                .movie(movie)
                .review(review)
                .score(0)
                .likeCount(0)
                .replyCount(0)
                .build();

        keywords.forEach(comment::addCommentKeyword);
        return comment;
    }
}
