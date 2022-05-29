package de.christophlorenz.rmmusic.web.model;

import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by clorenz on 09.06.15.
 */
public class Sticker implements Comparable<Sticker> {

    public String mediumCode;
    public Date date;
    public String formattedPrice;
    public String artist;
    public String title;

    public String getMediumCode() {
        return mediumCode;
    }

    public void setMediumCode(String mediumCode) {
        this.mediumCode = mediumCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int compareTo(Sticker sticker) {
        return StringUtils.compare(mediumCode.substring(0,1), sticker.getMediumCode().substring(0,1));
    }
}
