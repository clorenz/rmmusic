package de.christophlorenz.rmmusic.model;

import org.apache.commons.lang3.StringUtils;

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
 * Created by clorenz on 11.05.15.
 */
@Entity
@Table(name="SONG")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @ManyToOne(optional=false, targetEntity = Artist.class, fetch = FetchType.EAGER)
    @JoinColumn(name="artist_id", nullable=false)
    protected Artist artist;

    @Column(nullable = false)
    protected String title;

    @Column(length = 3)
    protected String release;

    @Column(name="\"year\"")
    protected Integer year;

    protected String authors;

    @Column(length=2)
    protected String dance;

    @Column(name="id3genre", length=24)
    protected String id3Genre;

    protected String remarks;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date timestamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getDance() {
        return dance;
    }

    public void setDance(String dance) {
        this.dance = dance;
        if (StringUtils.isBlank(dance)) {
            this.dance = null;
        }
     }

    public String getId3Genre() {
        return id3Genre;
    }

    public void setId3Genre(String id3Genre) {
        this.id3Genre = id3Genre;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", artist=" + artist +
                ", title='" + title + '\'' +
                ", release='" + release + '\'' +
                ", year=" + year +
                ", authors='" + authors + '\'' +
                ", dance='" + dance + '\'' +
                ", id3Genre='" + id3Genre + '\'' +
                ", remarks='" + remarks + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
