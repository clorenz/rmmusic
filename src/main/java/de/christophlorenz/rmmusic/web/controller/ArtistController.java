package de.christophlorenz.rmmusic.web.controller;

import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.persistence.jpa2.ArtistRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by clorenz on 21.05.15.
 */
@Controller
@RequestMapping("/rmmusic/artist")
public class ArtistController {

    private static final Logger log = Logger.getLogger(ArtistController.class);

    @Autowired
    ArtistRepository artistRepository;

    @RequestMapping("/select")
    public String selectArtist(Model model) {
        return "rmmusic/selectArtistForm";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editArtistById(@PathVariable(value="id") Long id,
                                 Model model) {
        List<Artist> artists = artistRepository.findById(id);
        if ( artists.isEmpty()) {
            // Should not be possible
            return "forward:/select";
        } else {
            return editFirstArtistInList(artists, model);
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editArtist(@RequestParam(value = "artist", required = true ) final String name,
                             @RequestParam(value = "exact", required = false) final String exact,
                             Model model) {

        List<Artist> artists = ("on".equalsIgnoreCase(exact)) ? artistRepository.findByName(name)
                    : artistRepository.findByNameIgnoreCaseStartingWithOrderByNameAsc(name);

        if ( artists.isEmpty()) {
            return editNewArtist(name, model);
        } else if ( artists.size()==1) {
            return editFirstArtistInList(artists, model);
        } else {
            model.addAttribute("artists", artists);
            return "rmmusic/artistList";
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editArtist(@Valid @ModelAttribute("artist") Artist artist,
                             BindingResult br,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        artist.setTimestamp(new Date());

        if ( br.hasErrors()) {
            return "rmmusic/artistEdit";
        }

        // Verify, that we don't have any other artist with the same name!
        if ( !artistRepository.findByName(artist.getName()).isEmpty()) {
            br.addError(new FieldError("name","name","Name is already used for another artist"));
            model.addAttribute("error","Name "+br.getRawFieldValue("name")+" is already used for another artist");
            return "rmmusic/artistEdit";
        }

        log.info("Submitted artist "+artist);

        artist = artistRepository.save(artist);
        redirectAttributes.addFlashAttribute("success", "Successfully saved artist "+artist.getName()+" with ID "+artist.getId());

        return "redirect:select";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteArtist(@Valid @RequestParam("id") Long id,
                               RedirectAttributes redirectAttributes) {
        log.info("Removing artist " + id);

        Artist artist = artistRepository.getOne(id);
        if ( artist!=null ) {
            try {
                artistRepository.delete(artist);
                redirectAttributes.addFlashAttribute("success", "Successfully removed artist " + artist.getName() + " with ID " + artist.getId());
                return "redirect:select";
            } catch (Exception e) {
                log.error("Cannot delete artist with id=" + id + ": ", e);
            }
        }

        redirectAttributes.addFlashAttribute("error", "Could not remove artist with id "+id);
        return "redirect:select";
    }

    private String editFirstArtistInList(List<Artist> artists, Model model) {
        Artist artist = artists.get(0);
        model.addAttribute("artist", artist);
        log.info("Editing artist="+artist);
        return "rmmusic/artistEdit";
    }


    private String editNewArtist(String name, Model model) {
        Artist artist = new Artist();
        artist.setName(name);
        artist.setPrint(calculatePrintName(name));
        model.addAttribute("artist", artist);
        return "rmmusic/artistEdit";
    }

    protected String calculatePrintName(String name) {
        if ( !StringUtils.isBlank(name)) {
            String[] parts = name.split("\\s*,\\s*");
            if ( parts.length==1) {
                return parts[0];
            } else if ( parts.length>=2) {
                ArrayUtils.reverse(parts,0,2);
                return StringUtils.join(parts," ");
            }
        }

        return name;
    }

}
