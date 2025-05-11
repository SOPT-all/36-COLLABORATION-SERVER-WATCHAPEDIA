package or.sopt.soptwatcha.domain;

import jakarta.persistence.*;
import lombok.*;
import or.sopt.soptwatcha.domain.common.BaseEntity;
import or.sopt.soptwatcha.domain.common.enums.Genre;
import or.sopt.soptwatcha.domain.common.enums.MovieType;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Movie extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String engTitle;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private LocalDate releaseYear;

    private int runningTime;

    private int ageLimit;

    private double score;

    private int isWished;

    private String details;

    private int collectionCount;

    @Enumerated(EnumType.STRING)
    private MovieType movieType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id")
    private Collection collection;
}
