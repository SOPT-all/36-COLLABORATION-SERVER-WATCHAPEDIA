package or.sopt.soptwatcha.repository;

import or.sopt.soptwatcha.domain.Movie;
import or.sopt.soptwatcha.domain.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieGenreRepository extends JpaRepository<MovieGenre, Long> {
    List<MovieGenre> findAllByMovie(Movie movie);
}