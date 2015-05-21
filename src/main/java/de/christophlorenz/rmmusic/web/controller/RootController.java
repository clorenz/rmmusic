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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                new SimpleDateFormat("dd.MM.yyyy"), false));
    }

    @RequestMapping("/")
    public void index(Model model) {
        model.addAttribute("version",VERSION);
    }


    @RequestMapping(value = "/artist/edit", method = RequestMethod.POST)
    public String editArtist(@Valid @ModelAttribute("artist") Artist artist,
                             BindingResult br,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        artist.setTimestamp(new Date());

        if ( br.hasErrors()) {
            return "rmmusic/artistEdit";
        }

        log.info("Submitted artist "+artist);

        artist = artistRepository.save(artist);
        redirectAttributes.addFlashAttribute("message", "Successfully saved artist "+artist.getName()+" with ID "+artist.getId());

        return "redirect:select";
    }
}
