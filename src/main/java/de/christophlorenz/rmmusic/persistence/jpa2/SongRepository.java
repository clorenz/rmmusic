package de.christophlorenz.rmmusic.persistence.jpa2;

import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by clorenz on 11.05.15.
 */
@RepositoryRestResource(collectionResourceRel="song", path="song")
public interface SongRepository extends JpaRepository<Song, Long>, SongRepositoryCustom {

    List<Song> findById(@Param("id") long id);

    List<Song> findByArtistIdInAndTitleIgnoreCaseContaining(@Param("artist_id")List<Long> artistIds, @Param("title")String title);

    List<Song> findByArtistAndTitleIgnoreCaseContaining(@Param("artist")Artist artist, @Param("title")String title);

    List<Song> findByArtistAndTitle(@Param("artist")Artist artist, @Param("title")String title);
}
