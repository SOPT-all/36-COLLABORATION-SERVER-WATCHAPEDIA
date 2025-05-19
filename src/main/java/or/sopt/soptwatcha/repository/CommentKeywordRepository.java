package or.sopt.soptwatcha.repository;

import or.sopt.soptwatcha.domain.Comment;
import or.sopt.soptwatcha.domain.CommentKeyword;
import or.sopt.soptwatcha.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentKeywordRepository extends JpaRepository<CommentKeyword, Long> {

    List<CommentKeyword> findByComment(Comment comment);
    List<CommentKeyword> findByKeyword(Keyword keyword);

    List<CommentKeyword> findByKeyword_Id(Long keywordId);
}
