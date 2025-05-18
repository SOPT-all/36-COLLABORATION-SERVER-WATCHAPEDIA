package or.sopt.soptwatcha.domain;

import jakarta.persistence.*;
import lombok.*;
import or.sopt.soptwatcha.domain.common.enums.Category;
import or.sopt.soptwatcha.domain.common.enums.IsPositive;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    @Enumerated(EnumType.STRING)
    private IsPositive isPositive;

    @Enumerated(EnumType.STRING)
    private UpperCategory upperCategory;

    @Enumerated(EnumType.STRING)
    private Category category;
}
