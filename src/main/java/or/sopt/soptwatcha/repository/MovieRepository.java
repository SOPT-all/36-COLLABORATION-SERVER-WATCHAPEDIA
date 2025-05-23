package or.sopt.soptwatcha.repository;

import or.sopt.soptwatcha.domain.Movie;
import or.sopt.soptwatcha.domain.common.enums.MovieType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findTop6ByOrderByScoreDesc();

    List<Movie> findTop6ByMovieTypeAndUploadYearGreaterThanEqualOrderByUploadYearAsc(MovieType movieType, LocalDate uploadYear);

}
