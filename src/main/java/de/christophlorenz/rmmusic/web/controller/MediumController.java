package de.christophlorenz.rmmusic.web.controller;

import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.model.Medium;
import de.christophlorenz.rmmusic.persistence.jpa2.ArtistRepository;
import de.christophlorenz.rmmusic.persistence.jpa2.MediaCodeComparator;
import de.christophlorenz.rmmusic.persistence.jpa2.MediumRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by clorenz on 27.05.15.
 */
@Controller
@RequestMapping("/rmmusic/medium")
public class MediumController {

    private static final Logger log = Logger.getLogger(MediumController.class);

    @Autowired
    MediumRepository mediumRepository;

    @Autowired
    ArtistRepository artistRepository;

    @RequestMapping("/single/select")
    public String selectSingle(Model model) {
        return "rmmusic/selectSingleForm";
    }

    @RequestMapping(value = "/single/edit", method = RequestMethod.GET)
    public String editSingle(@RequestParam(value="code", required = false) final String code,
                             @RequestParam(value="artist", required = false) final String artist,
                             @RequestParam(value="title", required = false) final String title,
                             @RequestParam(value="exact", required = false) final String exact,
                             Model model) {

        log.info("Code="+code+", artist="+artist+", title="+title+", exact="+exact);

        // If no parameter was set, throw an error
        if (StringUtils.isBlank(code) && StringUtils.isBlank(artist) && StringUtils.isBlank(title)) {
            model.addAttribute("error", "Please fill out at least one field");
            return "rmmusic/selectSingleForm";
        }

        // If "exact" was set, and no code was given but not both fields, artist and title, are set, throw an error
        if ( "on".equals(exact) && StringUtils.isBlank(code) && ( StringUtils.isBlank(artist) || StringUtils.isBlank(title)) ) {
            model.addAttribute("error", "If 'exact' is selected, you have to fill out both, artist and title or the code field");
            return "rmmusic/selectSingleForm";
        }

        if ( !StringUtils.isBlank(code)) {
            Medium medium = mediumRepository.findByTypeAndCode(Medium.SINGLE, code);
            model.addAttribute("medium", medium);
            return "rmmusic/editSingleForm";
        } else {
            List<Medium> media;
            if ( "on".equals(exact)) {
                media = mediumRepository.findByTypeAndArtistAndTitleExact(Medium.SINGLE, artist, title);
            } else {
                media = mediumRepository.findByTypeAndArtistAndTitle(Medium.SINGLE, artist, title);
            }

            if ( media.isEmpty()) {
                // Check, if artist is valid
                List<Artist> artists = artistRepository.findByName(artist);
                if ( artists.size()==1) {
                    return editNewSingle(model, artists.get(0), title);
                } else {
                    model.addAttribute("error", "Invalid or unknown artist '"+artist+"'");
                    return "rmmusic/selectSingleForm";
                }
            } else if ( media.size()==1) {
                return editFirstSingleInList(media, model);
            } else {
                Collections.sort(media, new MediaCodeComparator());
                model.addAttribute("media", media);
                return "rmmusic/mediaList";
            }

        }
    }


    @RequestMapping(value = "/single/edit/{id}", method = RequestMethod.GET)
    public String editSingleById(@PathVariable(value="id") Long id,
                                 Model model) {

        try {
            Medium medium = mediumRepository.getOne(id);
            log.debug("Medium=" + medium);
            model.addAttribute("medium", medium);
            return "rmmusic/editSingleForm";
        } catch ( Exception e) {
            log.warn("No medium with id="+id+" found!");
            model.addAttribute("error", "No single found with id="+id);
            return "forward:../select";
        }
    }


    @RequestMapping(value = "/single/edit", method = RequestMethod.POST)
    public String editSingle(@Valid @ModelAttribute("medium") Medium medium,
                             BindingResult br,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        medium.setTimestamp(new Date());

        log.info("Medium="+medium);

        if ( br.hasErrors()) {
            log.info("Errors="+br.toString());
            return "rmmusic/editSingleForm";
        }

        medium = mediumRepository.save(medium);
        redirectAttributes.addFlashAttribute("success", "Successfully saved medium "+medium.getCode());

        return "redirect:select";
    }

    private String editFirstSingleInList(List<Medium> media, Model model) {
        model.addAttribute("medium", media.get(0));
        return "rmmusic/editSingleForm";
    }

    protected String editNewSingle(Model model, Artist artist, String title) {
        Medium single = new Medium();
        single.setArtist(artist);
        single.setTitle(title);
        single.setCode(calculateCode(artist));

        model.addAttribute("single", single);
        return "rmmusic/editSingleForm";
    }


    protected String calculateCode(Artist artist) {
        // Check, if there are already media from this artist. If yes, use their code prefix and incrememnt the number

        // If not, generate up to 6 characters from the name parts (name, not print!)

        return null;
    }
}
