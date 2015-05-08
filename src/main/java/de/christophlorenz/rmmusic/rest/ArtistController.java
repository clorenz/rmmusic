package de.christophlorenz.rmmusic.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by clorenz on 30.04.15.
 */
@Controller
@RequestMapping("/artist")
public class ArtistController {


    @RequestMapping(value="/{id}", method= RequestMethod.GET)
    public HttpEntity<ArtistResource> artist(@PathVariable long id) {

        ArtistResource artist = new ArtistResource(id,"name","print","date","country","location","url","remarks");
        artist.add(linkTo(methodOn(ArtistController.class).artist(id)).withSelfRel());

        return new ResponseEntity<ArtistResource>(artist, HttpStatus.OK);
    }
}
