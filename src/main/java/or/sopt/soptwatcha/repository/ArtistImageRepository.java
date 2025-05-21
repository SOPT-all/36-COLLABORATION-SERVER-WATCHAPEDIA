package or.sopt.soptwatcha.repository;

import or.sopt.soptwatcha.domain.Artist;
import or.sopt.soptwatcha.domain.ArtistImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArtistImageRepository extends JpaRepository<ArtistImage, Long> {
    Optional<ArtistImage> findByFileName(String filename);
    List<ArtistImage> findAllByArtist(Artist artist);
}
