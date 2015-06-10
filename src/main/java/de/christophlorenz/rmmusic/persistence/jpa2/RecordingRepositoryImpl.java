package de.christophlorenz.rmmusic.persistence.jpa2;

import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.model.Recording;
import de.christophlorenz.rmmusic.model.Song;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Map<Integer,Long> countSongsOnMediaType() {
        Query query = em.createNativeQuery("select r.song_id,max(m.type) as bestmedia from recording r, medium m where m.id=medium_id group by r.song_id");
        List<Object[]> results = query.getResultList();

        Map<Integer,Long> countMap = new HashMap<Integer,Long>();

        for ( int i=0; i<results.size(); i++) {
            Object[] result = results.get(i);
            long songId = Long.parseLong(""+result[0]);
            int bestmedia = Integer.parseInt(""+result[1]);
            if ( countMap.get(bestmedia) == null) {
                countMap.put(bestmedia,1L);
            } else {
                long count = countMap.get(bestmedia);
                count++;
                countMap.put(bestmedia,count);
            }
        }

        return countMap;
    }

    @Override
    public Map<Integer, Long> countSongQualities() {
        Query query = em.createNativeQuery("select r.song_id,max(r.quality) as bestquality from recording r group by r.song_id");
        List<Object[]> results = query.getResultList();

        Map<Integer,Long> countMap = new HashMap<Integer,Long>();

        for ( int i=0; i<results.size(); i++) {
            Object[] result = results.get(i);
            long songId = Long.parseLong(""+result[0]);
            int bestquality = Integer.parseInt(""+result[1]);
            if ( countMap.get(bestquality) == null) {
                countMap.put(bestquality,1L);
            } else {
                long count = countMap.get(bestquality);
                count++;
                countMap.put(bestquality,count);
            }
        }

        return countMap;
    }

    @Override
    public Map<Integer, Long> countRecordingQualities() {
        Query query = em.createNativeQuery("select quality,count(*) from recording group by 1 order by 1");
        List<Object[]> results = query.getResultList();

        Map<Integer,Long> countMap = new HashMap<Integer,Long>();

        for ( int i=0; i<results.size(); i++) {
            Object[] result = results.get(i);
            int quality = Integer.parseInt(""+result[0]);
            long count = Long.parseLong(""+result[1]);
            countMap.put(quality,count);
        }

        return countMap;
    }


}
