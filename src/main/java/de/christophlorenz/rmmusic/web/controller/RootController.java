package de.christophlorenz.rmmusic.web.controller;

import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.persistence.jpa2.ArtistRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 * Created by clorenz on 15.05.15.
 */
@Controller
@RequestMapping("/rmmusic")
public class RootController {

    static final Logger log = Logger.getLogger(RootController.class);

    @Autowired
    ArtistRepository artistRepository;

    private static final String VERSION="0.01";

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                new SimpleDateFormat("yyyy-MM-dd"), false));
    }

    @RequestMapping("/")
    public void index(Model model) {
        model.addAttribute("version",VERSION);
    }

    @RequestMapping("/artist/select")
    public String selectArtist(Model model) {
        return "rmmusic/artistForm";
    }

    @RequestMapping(value = "/artist/edit", method = RequestMethod.GET)
    public String editArtist(@RequestParam(value = "artist", required = true ) final String name,
                             @RequestParam(value = "exact", required = false) final String exact,
                             Model model) {

        List<Artist> artists = ("on".equalsIgnoreCase(exact)) ? artistRepository.findByName(name)
                                                            : artistRepository.findByNameIgnoreCaseContaining(name);
        Artist artist;
        if ( artists.isEmpty()) {
            artist = new Artist();
            artist.setName(name);
            artist.setPrint(name);      // TODO
        } else {
            artist = artists.get(0);
        }

        model.addAttribute("artist", artist);

        log.info("Setting attribute artist="+artist);
        return "rmmusic/artistEdit";
    }

    @RequestMapping(value = "/artist/edit", method = RequestMethod.POST)
    public String editArtist(@Valid @ModelAttribute("artist") Artist artist,
                             BindingResult br,
                             Model model) {
        log.info("br="+br);
        if ( br.hasErrors()) {
            return "rmmusic/artistEdit";
        }

        log.info("Submitted artist "+artist);

        return "rmmusic/artistForm";
    }
}
