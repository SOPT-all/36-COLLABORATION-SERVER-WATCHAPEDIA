package or.sopt.soptwatcha.repository;

import or.sopt.soptwatcha.domain.Keyword;
import or.sopt.soptwatcha.domain.MovieKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieKeywordRepository extends JpaRepository<MovieKeyword, Long> {
    List<MovieKeyword> findByKeyword(Keyword keyword);
}
