package de.christophlorenz.rmmusic.rest;

import de.christophlorenz.rmmusic.persistence.jpa2.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by clorenz on 12.05.15.
 */
@RestController
@RequestMapping("/song")
public class SongController {

    @Autowired
    private SongRepository songRepository;          // <--- fails. WHY?????????????
}
