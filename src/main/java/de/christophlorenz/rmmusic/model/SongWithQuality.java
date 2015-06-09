package de.christophlorenz.rmmusic.model;

/**
 * Created by clorenz on 09.06.15.
 */
public class SongWithQuality extends Song{

    public int quality;
    public String mediumCode;

    public SongWithQuality(Song song) {
        setArtist(song.getArtist());
        setTimestamp(song.getTimestamp());
        setAuthors(song.getAuthors());
        setDance(song.getDance());
        setId(song.getId());
        setId3Genre(song.getId3Genre());
        setRelease(song.getRelease());
        setRemarks(song.getRemarks());
        setTitle(song.getTitle());
        setYear(song.getYear());
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public String getMediumCode() {
        return mediumCode;
    }

    public void setMediumCode(String mediumCode) {
        this.mediumCode = mediumCode;
    }
}
