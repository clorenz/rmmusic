package de.christophlorenz.rmmusic.persistence.jpa2;

import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clorenz on 11.05.15.
 */
@Repository
public class SongRepositoryImpl implements SongRepositoryCustom {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private SongRepository songRepository;

    @Override
    public List<Song> findByArtistNameIgnoreCaseContainingAndTitleIgnoreCaseContaining(String artistName, @Param("title") String title) {
        // Retrieve all matching artistIDs;
        List<Artist> artists = artistRepository.findByNameIgnoreCaseContaining(artistName);

        List<Long> artistIds = new ArrayList<Long>();
        for ( Artist artist : artists ) {
            artistIds.add(artist.getId());
        }

        System.out.println("ARtistIds="+artistIds);

        return songRepository.findByArtistIdInAndTitleIgnoreCaseContaining(artistIds, title);
    }


}
