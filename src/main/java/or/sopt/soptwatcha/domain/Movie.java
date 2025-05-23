package or.sopt.soptwatcha.domain;

import jakarta.persistence.*;
import lombok.*;
import or.sopt.soptwatcha.domain.common.BaseEntity;
import or.sopt.soptwatcha.domain.common.enums.FilmCountry;
import or.sopt.soptwatcha.domain.common.enums.MovieType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    private LocalDate releaseYear;

    @Column(name = "upload_year")
    private LocalDate uploadYear;

    private int runningTime;

    private int ageLimit;

    private double score;

    private int isWished;

    private String details;

    private int collectionCount;

    @Enumerated(EnumType.STRING)
    private FilmCountry filmCountry;

    @Enumerated(EnumType.STRING)
    private MovieType movieType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id")
    private Collection collection;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<MovieImage> movieImages = new ArrayList<>();

    @OneToMany(mappedBy = "movie",cascade = CascadeType.ALL)
    private List<MovieKeyword> movieKeywords = new ArrayList<>();
}
