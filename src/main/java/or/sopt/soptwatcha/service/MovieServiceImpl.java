package or.sopt.soptwatcha.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import or.sopt.soptwatcha.common.exception.CustomException;
import or.sopt.soptwatcha.common.exception.ErrorCode;
import or.sopt.soptwatcha.domain.Movie;
import or.sopt.soptwatcha.domain.MovieArtist;
import or.sopt.soptwatcha.domain.MovieGenre;
import or.sopt.soptwatcha.domain.MovieImage;
import or.sopt.soptwatcha.dto.response.MovieDetailResponseDTO;
import or.sopt.soptwatcha.repository.MovieArtistRepository;
import or.sopt.soptwatcha.repository.MovieGenreRepository;
import or.sopt.soptwatcha.repository.MovieImageRepository;
import or.sopt.soptwatcha.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MovieServiceImpl {

    private final MovieRepository movieRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final MovieArtistRepository movieArtistRepository;
    private final MovieImageRepository movieImageRepository;

    public MovieDetailResponseDTO getMovieDetail(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REQUEST, "해당 영화를 찾을 수 없습니다."));

        List<MovieGenre> movieGenres = movieGenreRepository.findAllByMovie(movie);
        List<MovieArtist> movieArtists = movieArtistRepository.findAllByMovie(movie);
        List<MovieImage> movieImages = movieImageRepository.findAllByMovie(movie);

        return MovieDetailResponseDTO.from(movie, movieGenres, movieArtists, movieImages);
    }
}