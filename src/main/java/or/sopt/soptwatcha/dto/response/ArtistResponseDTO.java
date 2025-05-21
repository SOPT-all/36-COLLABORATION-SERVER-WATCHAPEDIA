package or.sopt.soptwatcha.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import or.sopt.soptwatcha.domain.ArtistImage;
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
    private String imagePath;

    public static ArtistResponseDTO from(MovieArtist movieArtist) {
        String imagePath = movieArtist.getArtist().getArtistImages().stream()
                .findFirst()
                .map(ArtistImage::getImageLink)
                .orElse(null);

        return ArtistResponseDTO.builder()
                .role(movieArtist.getRole())
                .name(movieArtist.getArtist().getName())
                .character(movieArtist.getCharacterName())
                .imagePath(imagePath)
                .build();
    }
}