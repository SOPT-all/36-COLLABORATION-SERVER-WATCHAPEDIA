package or.sopt.soptwatcha.dto.response;

import lombok.*;

import java.awt.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageUploadResponseDTO {

    private String filename;
    private String originalFilename;
    private String imagePath;

    public static ImageUploadResponseDTO from(String filename, String originalFilename, String imagePath) {
        return ImageUploadResponseDTO.builder()
                .filename(filename)
                .originalFilename(originalFilename)
                .imagePath(imagePath)
                .build();
    }
}
