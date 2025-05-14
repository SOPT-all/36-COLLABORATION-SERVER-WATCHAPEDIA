package or.sopt.soptwatcha.repository;

import or.sopt.soptwatcha.domain.MovieImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieImageRepository extends JpaRepository<MovieImage, Long> {

    Optional<MovieImage> findByFileName(String filename);
}
