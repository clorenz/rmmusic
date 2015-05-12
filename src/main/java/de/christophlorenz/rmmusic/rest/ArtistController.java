package de.christophlorenz.rmmusic.rest;

import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.persistence.jpa2.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by clorenz on 12.05.15.
 */
@RestController
@RequestMapping("/artist")
public class ArtistController {

    @Autowired
    private ArtistRepository artistRepository;

    @RequestMapping(method= RequestMethod.GET)
    public List<Artist> getAll() {
        return artistRepository.findAll();
    }
}
