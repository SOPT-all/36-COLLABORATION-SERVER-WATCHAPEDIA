package or.sopt.soptwatcha.repository;

import or.sopt.soptwatcha.domain.Movie;
import or.sopt.soptwatcha.domain.MovieArtist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieArtistRepository extends JpaRepository<MovieArtist, Long> {
    List<MovieArtist> findAllByMovie(Movie movie);
}