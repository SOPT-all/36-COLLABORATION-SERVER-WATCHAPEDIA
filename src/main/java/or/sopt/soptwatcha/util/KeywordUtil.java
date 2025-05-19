package or.sopt.soptwatcha.util;

import or.sopt.soptwatcha.domain.Keyword;
import org.springframework.stereotype.Component;

import java.util.List;

public interface KeywordUtil {
    List<Keyword> getPriorityOrderedKeywords(List<Keyword> keywords);

    List<Keyword> getPositive(List<Keyword> keywords);

    String makeRankingDescription(Keyword keyword);
}
