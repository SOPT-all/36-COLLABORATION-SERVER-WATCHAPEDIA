package or.sopt.soptwatcha.repository;

import or.sopt.soptwatcha.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findTop5ByOrderByExpectScoreDesc();

}
