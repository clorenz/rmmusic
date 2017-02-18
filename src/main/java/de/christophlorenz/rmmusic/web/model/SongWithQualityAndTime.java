package de.christophlorenz.rmmusic.web.model;

import de.christophlorenz.rmmusic.model.Song;

/**
 * Created by clorenz on 18.02.17.
 */
public class SongWithQualityAndTime extends SongWithQuality {

    public int time;

    public SongWithQualityAndTime(Song song) {
        super(song);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTimeFormatted() {
        if ( time>0) {
            int minutes = (int) Math.floor((double) time / 60d);
            int seconds = time % 60;
            return String.format("%d:%02d", minutes, seconds);
        } else {
            return "";
        }
    }
}
