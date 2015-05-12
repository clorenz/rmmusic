package de.christophlorenz.rmmusic.persistence.jpa2;

import de.christophlorenz.rmmusic.model.Recording;

import java.util.List;

/**
 * Created by clorenz on 12.05.15.
 */
public interface RecordingRepositoryCustom {

    List<Recording> findByArtistNameIgnoreCaseContainingAndAndSongTitleIgnoreCaseContaining(String artistName, String songTitle);
}
