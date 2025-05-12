package or.sopt.soptwatcha.repository;

import or.sopt.soptwatcha.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
