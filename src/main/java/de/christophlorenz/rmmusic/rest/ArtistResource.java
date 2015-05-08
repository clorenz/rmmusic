package de.christophlorenz.rmmusic.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

/**
 * Created by clorenz on 30.04.15.
 */
public class ArtistResource extends ResourceSupport {

    private final String name;
    private final String print;
    private final String date;
    private final String country;
    private final String location;
    private final String url;
    private final String remarks;

    @JsonCreator
    public ArtistResource(@JsonProperty("id") long id,
                  @JsonProperty("name") String name,
                  @JsonProperty("print") String print,
                  @JsonProperty("date") String date,
                  @JsonProperty("country") String country,
                  @JsonProperty("location") String location,
                  @JsonProperty("url") String url,
                  @JsonProperty("remarks") String remarks) {
        this.country=country;
        this.date=date;
        this.location=location;
        this.name=name;
        this.print=print;
        this.remarks=remarks;
        this.url=url;
    }

    public String getName() {
        return name;
    }

    public String getPrint() {
        return print;
    }

    public String getDate() {
        return date;
    }

    public String getCountry() {
        return country;
    }

    public String getLocation() {
        return location;
    }

    public String getUrl() {
        return url;
    }

    public String getRemarks() {
        return remarks;
    }
    
}
