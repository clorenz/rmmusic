package de.christophlorenz.rmmusic.persistence.jpa2;

import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.model.Song;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by clorenz on 11.05.15.
 */
@Repository
@RepositoryRestResource(collectionResourceRel="song", path="song")
@Configurable
public class SongRepositoryImpl implements SongRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Song> findByArtistNameIgnoreCaseContainingAndTitleIgnoreCaseContaining(String artistName, @Param("title") String title) {
        // Retrieve all matching artistIDs;
        // TODO: Case insensitive!
        List<Artist> artists = em.createQuery("select a from Artist a where a.name like :artistName")
                                .setParameter("artistName", "%"+artistName+"%")
                                .getResultList();

        return em.createQuery("select s from Song s where s.artist in :artists")
                .setParameter("artists", artists)
                .getResultList();
    }


}
