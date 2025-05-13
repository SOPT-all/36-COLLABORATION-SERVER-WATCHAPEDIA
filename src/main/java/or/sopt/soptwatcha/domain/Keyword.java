package or.sopt.soptwatcha.domain;

import jakarta.persistence.*;
import lombok.*;
import or.sopt.soptwatcha.domain.common.enums.Category;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;
}
