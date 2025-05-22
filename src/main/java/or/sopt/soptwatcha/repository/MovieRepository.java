package or.sopt.soptwatcha.repository;

import or.sopt.soptwatcha.domain.Movie;
import or.sopt.soptwatcha.domain.common.enums.MovieType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findTop5ByOrderByScoreDesc();

    List<Movie> findTop5ByMovieTypeAndUploadYearGreaterThanEqualOrderByUploadYearAsc(MovieType movieType, LocalDate uploadYear);




}
