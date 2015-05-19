package de.christophlorenz.rmmusic.model;

import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by clorenz on 05.05.15.
 */
@Entity
@Table(name="ARTIST")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    private String name;
    private String print;
    @Temporal(TemporalType.DATE)
    private Date birthday;
    private String country;
    private String location;
    private String url;
    private String remarks;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrint() {
        return print;
    }

    public void setPrint(String print) {
        this.print = print;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /*
    public void setBirthday(String bday) {
        if ( bday!=null && bday.indexOf("-")>-1) {
            birthday = INTL_FORMAT.parse(bday);
        } else if ( bday!=null && bday.indexOf(".")>-1) {
            birthday = GERMAN_FORMAT.parse(bday);
        }
    }
    */

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", print='" + print + '\'' +
                ", birthday=" + birthday +
                ", country='" + country + '\'' +
                ", location='" + location + '\'' +
                ", url='" + url + '\'' +
                ", remarks='" + remarks + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
