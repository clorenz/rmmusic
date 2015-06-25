package de.christophlorenz.rmmusic.web.model;

import de.christophlorenz.rmmusic.model.Recording;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by clorenz on 30.05.15.
 */
public class RecordingsCounterComparator implements Comparator<Recording>, Serializable {

    @Override
    public int compare(Recording o1, Recording o2) {
        String counter1 = o1.getCounter()!=null?o1.getCounter().replaceAll("\\D",""):"";
        String counter2 = o2.getCounter()!=null?o2.getCounter().replaceAll("\\D",""):"";

        try {
            int c1 = Integer.parseInt(counter1);
            int c2 = Integer.parseInt(counter2);

            return Integer.compare(c1, c2);
        } catch (Exception e) {
            return counter1.compareTo(counter2);
        }
    }
}
