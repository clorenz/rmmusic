package de.christophlorenz.rmmusic.persistence.jpa2;

import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.model.Song;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by clorenz on 11.05.15.
 */
public interface SongRepositoryCustom {

    List<Song> findByArtistNameIgnoreCaseContainingAndTitleIgnoreCaseContaining(String artistName, @Param("title")String title);
}
