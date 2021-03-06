package de.christophlorenz.rmmusic.model;

import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by clorenz on 05.05.15.
 */
@Entity
@Table(name="ARTIST", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
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

    public void setId(long id) {
        this.id=id;
    }

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

    @PostLoad
    public void trimCountryAndFixInvalidBirthdays() {
        country = StringUtils.trimAllWhitespace(country);
        if ( birthday!=null ) {
            Calendar birthdayCalendar = new GregorianCalendar();
            birthdayCalendar.setTime(birthday);
            if (birthdayCalendar.get(Calendar.YEAR) <= 1) {
                birthday = null;
            }
        }
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
