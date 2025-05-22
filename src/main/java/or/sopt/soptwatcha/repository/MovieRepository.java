package or.sopt.soptwatcha.repository;

import or.sopt.soptwatcha.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findTop5ByOrderByScoreDesc();

    @Query(
            value = "SELECT * FROM movie " +
                    "WHERE movie_type = :movieType AND release_year >= :targetDate " +
                    "ORDER BY DATEDIFF(release_year, :targetDate) ASC LIMIT 5",
            nativeQuery = true
    )
    List<Movie> findTop5ByClosestToDateAndMovieType(@Param("targetDate") LocalDate targetDate,
                                                    @Param("movieType") String movieType);


}
