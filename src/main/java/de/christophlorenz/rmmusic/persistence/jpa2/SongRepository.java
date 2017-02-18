package de.christophlorenz.rmmusic.persistence.jpa2;

import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.model.Song;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by clorenz on 11.05.15.
 */
@RepositoryRestResource(collectionResourceRel="song", path="song")
@Configurable
public interface SongRepository extends JpaRepository<Song, Long>, SongRepositoryCustom {

    List<Song> findById(@Param("id") long id);

    List<Song> findByArtistIdInAndTitleIgnoreCaseContaining(@Param("artist_id")List<Long> artistIds, @Param("title")String title);

    List<Song> findByArtistAndTitleIgnoreCaseContaining(@Param("artist")Artist artist, @Param("title")String title);

    List<Song> findByArtistAndTitle(@Param("artist")Artist artist, @Param("title")String title);

    List<Song> findByArtistNameAndTitleIgnoreCaseStartingWith(@Param("artist_name") String artistName, @Param("title") String title);

    List<Song> findByArtistNameIgnoreCaseStartingWithAndTitleIgnoreCaseStartingWithOrderByArtistAscTitleAsc(@Param("artist_name") String artistName, @Param("title") String title);

    List<Song> findByArtistNameIgnoreCaseStartingWithAndTitleIgnoreCaseContainingAndAuthorsIgnoreCaseContainingOrderByArtistAscTitleAsc(@Param("artist_name") String artist, @Param("title")String title, @Param("authors")String authors);
}
