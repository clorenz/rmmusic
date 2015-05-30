package de.christophlorenz.rmmusic.web.model;

import de.christophlorenz.rmmusic.model.Recording;

import java.util.Comparator;

/**
 * Created by clorenz on 30.05.15.
 */
public class RecordingsSideAndTrackComparator implements Comparator<Recording> {


    @Override
    public int compare(Recording o1, Recording o2) {
        String side1 = o1.getSide()!=null?o1.getSide().toUpperCase().trim():"";
        String side2 = o2.getSide()!=null?o2.getSide().toUpperCase().trim():"";
        Integer track1 = o1.getTrack() != null ? o1.getTrack() : 0;
        Integer track2 = o2.getTrack() != null ? o2.getTrack() : 0;

        if ( side1.equals(side2)) {
            return track1.compareTo(track2);
        }

        return side1.compareTo(side2);
    }
}
