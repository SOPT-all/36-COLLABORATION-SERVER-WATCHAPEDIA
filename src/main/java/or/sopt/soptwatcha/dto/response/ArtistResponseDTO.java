package or.sopt.soptwatcha.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import or.sopt.soptwatcha.domain.MovieArtist;
import or.sopt.soptwatcha.domain.common.enums.Role;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistResponseDTO {
    private Role role;
    private String name;
    private String character;

    public static ArtistResponseDTO from(MovieArtist movieArtist) {
        return ArtistResponseDTO.builder()
                .role(movieArtist.getRole())
                .name(movieArtist.getArtist().getName())
                .character(movieArtist.getCharacterName())
                .build();
    }
}