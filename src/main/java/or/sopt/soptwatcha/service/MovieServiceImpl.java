package or.sopt.soptwatcha.service;

import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieService {


    public void getPreferenceMovies(){
        /**
         * 1. 파라미터로 코멘트 식별자를 받아서
         * 2. 해당 코멘트가 존재하는 키워드를 찾는다
         * 3. 그리고 그 키워드들을
         *  3-1. 긍정/부정으로 가르고 부정은 날려버림
         *  3-2. 키워드 별로 우선순위를 책정함
         *
         * 4. 그리고 그 키워드대로 Movie에 어떤게 매핑되어 있는지 찾아보고 상위 네 개 리스트업
         *  4-1. 이때 DTO 하나 쓰고
         * 5. 그리고 그걸 전부 감싸서 반환
         *  5-1. 이때 DTO 하나 더
         *
         * 그럼 필요한 repository가
         *  1. commentRepository
         *  2. commentKeywordRepository
         *  3. keywordRepository
         *  4. movieRepository
         *  5. movieImageRepository
         *  6. movieKeywordRepsoitory
         * */


    }
}
