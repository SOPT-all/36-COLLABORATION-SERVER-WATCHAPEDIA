package or.sopt.soptwatcha.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import or.sopt.soptwatcha.domain.Movie;
import or.sopt.soptwatcha.domain.MovieArtist;
import or.sopt.soptwatcha.domain.MovieGenre;
import or.sopt.soptwatcha.domain.MovieImage;
import or.sopt.soptwatcha.domain.common.enums.Category;
import or.sopt.soptwatcha.domain.common.enums.MovieImageType;
import or.sopt.soptwatcha.util.TimeFormatUtil;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDetailResponseDTO {
    private Long id;
    private String title;
    private String engTitle;
    private List<String> genres;
    private Integer releaseYear;
    private String runningTime;
    private Integer ageLimit;
    private String detail;
    private String posterImage;
    private String detailImage;
    private String country;
    private List<ArtistResponseDTO> artists;
    private List<String> keywords;

    public static MovieDetailResponseDTO from(Movie movie, 
                                             List<MovieGenre> movieGenres, 
                                             List<MovieArtist> movieArtists, 
                                             List<MovieImage> movieImages) {
        
        List<String> genreList = movieGenres.stream()
                .map(movieGenre -> movieGenre.getGenre().getValue())
                .collect(Collectors.toList());

        List<ArtistResponseDTO> artistList = movieArtists.stream()
                .map(ArtistResponseDTO::from)
                .collect(Collectors.toList());

        String posterImageUrl = movieImages.stream()
                .filter(image -> image.getMovieImageType() == MovieImageType.POSTER)
                .findFirst()
                .map(MovieImage::getImageLink)
                .orElse(null);

        String detailImageUrl = movieImages.stream()
                .filter(image -> image.getMovieImageType() == MovieImageType.DESCRIPTION)
                .findFirst()
                .map(MovieImage::getImageLink)
                .orElse(null);

        List<String> keywordList = movie.getMovieKeywords().stream()
                .map(movieKeyword -> movieKeyword.getKeyword())
                .filter(keyword -> keyword.getCategory() == Category.MOVIE_KEYWORD)
                .map(keyword -> keyword.getValue())
                .collect(Collectors.toList());

        return MovieDetailResponseDTO.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .engTitle(movie.getEngTitle())
                .genres(genreList)
                .releaseYear(movie.getReleaseYear().getYear())
                .runningTime(TimeFormatUtil.formatMinutesToHoursAndMinutes(movie.getRunningTime()))
                .ageLimit(movie.getAgeLimit())
                .detail(movie.getDetails())
                .posterImage(posterImageUrl)
                .detailImage(detailImageUrl)
                .country(movie.getFilmCountry().getDisplayName())
                .artists(artistList)
                .keywords(keywordList)
                .build();
    }

}