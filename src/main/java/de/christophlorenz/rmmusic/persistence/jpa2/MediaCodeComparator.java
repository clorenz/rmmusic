package de.christophlorenz.rmmusic.persistence.jpa2;

import de.christophlorenz.rmmusic.model.Medium;

import java.util.Comparator;

/**
 * Created by clorenz on 27.05.15.
 */
public class MediaCodeComparator implements Comparator<Medium> {

    @Override
    /**
     * Compares only the numerical values!
     */
    public int compare(Medium o1, Medium o2) {
        String code1 = o1.getCode().replaceAll("\\D","");
        String code2 = o2.getCode().replaceAll("\\D","");

        try {
            int c1 = Integer.parseInt(code1);
            int c2 = Integer.parseInt(code2);

            return Integer.compare(c1, c2);
        } catch (Exception e) {
            return code1.compareTo(code2);
        }
    }
}
