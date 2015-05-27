package de.christophlorenz.rmmusic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by clorenz on 27.05.15.
 */

/**
 * Created as view: create view label as select distinct(label) from medium;
 */
@Entity
@Table(name="LABEL")
public class Label implements Serializable {


    @Id
    @Column(insertable = false, updatable = false)
    private String label;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
