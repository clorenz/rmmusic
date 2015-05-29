package de.christophlorenz.rmmusic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by clorenz on 12.05.15.
 */
@Entity()
@Table(name="RECORDING")
public class Recording {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional=false, targetEntity = Song.class, fetch = FetchType.EAGER)
    @JoinColumn(name="song_id", nullable=false)
    private Song song;


    @ManyToOne(optional=false, targetEntity = Medium.class, fetch = FetchType.EAGER)
    @JoinColumn(name="medium_id", nullable=false)
    private Medium medium;

    @Column(length = 1)
    private String side;

    private Integer track;

    private String counter;

    private Integer time;

    @Column(name = "recording_year")
    private Integer recordingYear;

    @Column(length = 2)
    private String longplay;

    private Integer quality;

    private String remarks;

    private Integer special;

    @Column(length = 3)
    private String digital;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Medium getMedium() {
        return medium;
    }

    public void setMedium(Medium medium) {
        this.medium = medium;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Integer getTrack() {
        return track;
    }

    public void setTrack(Integer track) {
        this.track = track;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getRecordingYear() {
        return recordingYear;
    }

    public void setRecordingYear(Integer recordingYear) {
        this.recordingYear = recordingYear;
    }

    public String getLongplay() {
        return longplay;
    }

    public void setLongplay(String longplay) {
        this.longplay = longplay;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getSpecial() {
        return special;
    }

    public void setSpecial(Integer special) {
        this.special = special;
    }

    public String getDigital() {
        return digital;
    }

    public void setDigital(String digital) {
        this.digital = digital;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimeFormatted() {
        int minutes = (int) Math.floor((double)time/60d);
        int seconds = time % 60;
        return new String().format("%d:%02d", minutes, seconds);
    }
}
