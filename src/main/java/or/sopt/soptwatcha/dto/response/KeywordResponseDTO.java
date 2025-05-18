package or.sopt.soptwatcha.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import or.sopt.soptwatcha.domain.Keyword;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeywordResponseDTO {

    private String keyword;

    public static KeywordResponseDTO of(Keyword keyword) {
        return KeywordResponseDTO.builder()
                .keyword(keyword.getValue())
                .build();
    }
}
