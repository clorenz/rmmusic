package de.christophlorenz.rmmusic.persistence.jpa2;

import de.christophlorenz.rmmusic.model.Recording;
import de.christophlorenz.rmmusic.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by clorenz on 12.05.15.
 */
@RepositoryRestResource(collectionResourceRel="recording", path="recording")
public interface RecordingRepository extends JpaRepository<Recording,Long>, RecordingRepositoryCustom {

    List<Recording> findById(@Param("id") long id);

    @Query(value = "Select r from Recording r where r.medium.id=:medium_id order by lower(r.side), r.track, r.counter asc")
    List<Recording> findByMediumIdOrderBySideTrackCounterAsc(@Param("medium_id") long medium_id);

    List<Recording> findBySong(@Param("song")Song song);

    List<Recording> findBySongIdIn(@Param("song_id") List<Long> songId);
}
