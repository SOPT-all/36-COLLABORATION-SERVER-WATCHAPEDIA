package or.sopt.soptwatcha.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import or.sopt.soptwatcha.domain.Movie;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetMovieSoonResponseDTO {

    private int untilRelease;

    private int isWishedCount;

    private String imagePath;

    private String title;

    private LocalDate releaseYear;

    public static GetMovieSoonResponseDTO from(Movie movie, String imagePath,int untilRelease) {

        return GetMovieSoonResponseDTO.builder()
                .untilRelease(untilRelease)
                .isWishedCount(movie.getIsWished())
                .imagePath(imagePath)
                .title(movie.getTitle())
                .releaseYear(movie.getReleaseYear())
                .build();
    }



    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetMovieSoonResponseListDTO{

        private List<GetMovieSoonResponseDTO> soons;

    }
}


