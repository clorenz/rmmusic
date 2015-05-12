package de.christophlorenz.rmmusic.persistence.jpa2;

import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.model.Recording;
import de.christophlorenz.rmmusic.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by clorenz on 12.05.15.
 */
@Repository
public class RecordingRepositoryImpl implements RecordingRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Recording> findByArtistNameIgnoreCaseContainingAndAndSongTitleIgnoreCaseContaining(String artistName, String songTitle) {
        // TODO: Case insensitive
        List<Artist> artists = em.createQuery("select a from Artist a where a.name like :artistName")
                                    .setParameter("artistName", "%"+artistName+"%")
                                    .getResultList();

        // TODO: Case insensitive
        List<Song> songs = em.createQuery("select s from Song s where s.artist in :artists and title like :songTitle")
                                    .setParameter("artists", artists)
                                    .setParameter("songTitle", "%"+songTitle+"%")
                                    .getResultList();

        return em.createQuery("select r from Recording r where r.song in :songs")
                .setParameter("songs", songs)
                .getResultList();
    }
}
