package de.christophlorenz.rmmusic.web.model;

import de.christophlorenz.rmmusic.model.Recording;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by clorenz on 30.05.15.
 */
public class RecordingsTrackComparator implements Comparator<Recording>, Serializable {


    @Override
    public int compare(Recording o1, Recording o2) {
        Integer track1 = o1.getTrack() != null ? o1.getTrack() : 0;
        Integer track2 = o2.getTrack() != null ? o2.getTrack() : 0;

        return Integer.compare(track1, track2);
    }
}
