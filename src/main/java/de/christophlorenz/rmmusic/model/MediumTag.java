package de.christophlorenz.rmmusic.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created by clorenz on 13.05.15.
 */
@Entity
@Table(name="TAG_MEDIUM", uniqueConstraints = @UniqueConstraint(columnNames = {"username","mediumid"}))
public class MediumTag {

    @EmbeddedId
    private TagMediumId id;

    private String action;

    public TagMediumId getId() {
        return id;
    }

    public void setId(TagMediumId id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}