package or.sopt.soptwatcha.repository;

import or.sopt.soptwatcha.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}