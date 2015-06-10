package de.christophlorenz.rmmusic.persistence.jpa2;

import de.christophlorenz.rmmusic.model.Recording;

import java.util.List;
import java.util.Map;

/**
 * Created by clorenz on 12.05.15.
 */
public interface RecordingRepositoryCustom {

    List<Recording> findByArtistNameIgnoreCaseContainingAndAndSongTitleIgnoreCaseContaining(String artistName, String songTitle);

    Map<Integer,Long> countSongsOnMediaType();

    Map<Integer,Long> countSongQualities();

    Map<Integer,Long> countRecordingQualities();
}
