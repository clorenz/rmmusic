package de.christophlorenz.rmmusic.web.model;

import de.christophlorenz.rmmusic.model.Recording;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by clorenz on 30.05.15.
 */
public class RecordingsSideComparator implements Comparator<Recording>, Serializable {


    @Override
    public int compare(Recording o1, Recording o2) {
        String side1 = o1.getSide()!=null?o1.getSide().toUpperCase().trim():"";
        String side2 = o2.getSide()!=null?o2.getSide().toUpperCase().trim():"";

        return side1.compareTo(side2);
    }
}
