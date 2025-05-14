package or.sopt.soptwatcha.domain;

import jakarta.persistence.*;
import lombok.*;
import or.sopt.soptwatcha.domain.common.BaseEntity;
import or.sopt.soptwatcha.domain.common.enums.*;

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
}
