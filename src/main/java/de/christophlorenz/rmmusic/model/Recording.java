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

    public String getPosition() {
        StringBuffer position = new StringBuffer();
        position.append(medium.getTypeCode());
        position.append(" ");
        position.append(medium.getCode());
        position.append(" / ");
        if ( side!=null) {
            position.append(side);
        }
        if ( track!=null && track!=0 ) {
            position.append(""+track);          // toString();
        }
        if ( counter!=null) {
            if ( side!=null ) {
                position.append(" ");
            }
            position.append(counter);
        }
        return position.toString();
    }

    public String getTimeFormatted() {
        if ( time!=null) {
            int minutes = (int) Math.floor((double) time / 60d);
            int seconds = time % 60;
            return new String().format("%d:%02d", minutes, seconds);
        } else {
            return "";
        }
    }

    public void setTimeFormatted(String timeFormatted) {
        if ( timeFormatted!=null) {
            String[] parts = timeFormatted.split(":");
            time = Integer.parseInt(parts[0]);
            if (parts.length == 2)
                time = 60 * time + (int) Integer.parseInt(parts[1]);
        }
    }

    public Boolean getQualityStereo() {
        return ( quality==null ? Boolean.TRUE : (quality & 1) == 1);
    }

    public void setQualityStereo(boolean stereo) {
        if ( quality==null) { quality=0; }
        quality &= ~(1<<0);                     // Bit löschen
        quality |= (stereo?1:0)<<0;             // ggf. Bit setzen
    }

    public Boolean getQualityNoisefree() {
        return ( quality==null ? Boolean.TRUE : (quality & 2) == 2);
    }

    public void setQualityNoisefree(boolean noisefree) {
        if ( quality==null) { quality=0; }
        quality &= ~(1<<1);                     // Bit löschen
        quality |= (noisefree?1:0)<<1;          // ggf. Bit setzen
    }

    public Boolean getQualityComplete() {
        return ( quality==null ? Boolean.TRUE : (quality & 4) == 4);
    }

    public void setQualityComplete(boolean complete) {
        if ( quality==null) { quality=0; }
        quality &= ~(1<<2);             // Bit löschen
        quality |= (complete?1:0)<<2;   // ggf. Bit setzen
    }

    public Boolean getSpecialMaxi() {
        return ( special!=null && (special & 1) == 1);
    }

    public void setSpecialMaxi(boolean maxi) {
        if ( special==null) { special=0; }
        special &= ~(1<<0);         // Bit löschen
        special |= (maxi?1:0)<<0;  // ggf. Bit setzen
    }

    public Boolean getSpecialLive() {
        return ( special != null && (special & 2) == 2);
    }

    public void setSpecialLive(boolean live) {
        if ( special==null) { special=0; }
        special &= ~(1<<1);         // Bit löschen
        special |= (live?1:0)<<1;   // ggf. Bit setzen
    }

    public Boolean getSpecialRemix() {
        return ( special != null && (special & 4) == 4);
    }

    public void setSpecialRemix(boolean remix) {
        if ( special==null) { special=0; }
        special &= ~(1<<2);         // Bit löschen
        special |= (remix?1:0)<<2;  // ggf. Bit setzen
    }

    public Boolean getSpecialVideo() {
        return ( special != null && (special & 8) == 8);
    }

    public void setSpecialVideo(boolean video) {
        if ( special==null) { special=0; }
        special &= ~(1<<3);         // Bit löschen
        special |= (video?1:0)<<3;  // ggf. Bit setzen
    }

    public String getVerboseQuality() {
        StringBuffer ret = new StringBuffer();
        if ( getQualityStereo()) {
            ret.append("Stereo");
        }
        if ( getQualityNoisefree()) {
            if ( ret.length()>0) {
                ret.append(", ");
            }
            ret.append("Noisefree");
        }
        if ( getQualityComplete()) {
            if ( ret.length()>0) {
                ret.append(", ");
            }
            ret.append("Complete");
        }
        return ret.toString();
    }

    public String getVerboseSpecial() {
        StringBuffer ret = new StringBuffer();
        if ( getSpecialMaxi()) {
            ret.append("Maxi");
        }
        if ( getSpecialLive()) {
            if ( ret.length()>0) {
                ret.append(", ");
            }
            ret.append("Live");
        }
        if ( getSpecialRemix()) {
            if ( ret.length()>0) {
                ret.append(", ");
            }
            ret.append("Remix");
        }
        if ( getSpecialVideo()) {
            if ( ret.length()>0) {
                ret.append(", ");
            }
            ret.append("Video");
        }
        return ret.toString();
    }

    @Override
    public String toString() {
        return "Recording{" +
                "id=" + id +
                ", song=" + song +
                ", medium=" + medium +
                ", side='" + side + '\'' +
                ", track=" + track +
                ", counter='" + counter + '\'' +
                ", time=" + time +
                ", recordingYear=" + recordingYear +
                ", longplay='" + longplay + '\'' +
                ", quality=" + quality +
                ", remarks='" + remarks + '\'' +
                ", special=" + special +
                ", digital='" + digital + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
