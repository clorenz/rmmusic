package de.christophlorenz.wmmusic.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import de.christophlorenz.wmmusic.model.Artist;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by clorenz on 30.04.15.
 */
@Controller
public class ArtistController {

    @RequestMapping("/artist")
    @ResponseBody
    public HttpEntity<Artist> artist(
            @RequestParam(value="id", required=true) long id) {

        Artist artist = new Artist(id, "name", "print", "date", "country", "location", "url", "remarks");
        artist.add(linkTo(methodOn(Artist.class).getId()).withSelfRel());

        return new ResponseEntity<Artist>(artist, HttpStatus.OK);
    }
}
