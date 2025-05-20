package or.sopt.soptwatcha.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequestDTO {
    @NotEmpty(message = "키워드 ID는 최소 1개 이상 필요합니다")
    private List<Long> keywordIds;
    
    private String review;
}