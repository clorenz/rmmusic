package de.christophlorenz.rmmusic.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Created by clorenz on 13.05.15.
 */
@Embeddable
public class TagMediumId implements Serializable {

    @Column(name="username")
    private String userName;

    @Column(name="mediumid")
    private Long mediumId;

    public TagMediumId() {}

    public TagMediumId(String userName, Long mediumId) {
        this.userName = userName;
        this.mediumId = mediumId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getMediumId() {
        return mediumId;
    }

    public void setMediumId(Long mediumId) {
        this.mediumId = mediumId;
    }

    @Override
    public String toString() {
        return userName+"-"+mediumId;
    }
}
