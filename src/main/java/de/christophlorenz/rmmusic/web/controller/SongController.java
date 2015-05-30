package de.christophlorenz.rmmusic.web.controller;

import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.model.Song;
import de.christophlorenz.rmmusic.persistence.jpa2.ArtistRepository;
import de.christophlorenz.rmmusic.persistence.jpa2.SongRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by clorenz on 29.05.15.
 */
@Controller
@RequestMapping("/rmmusic/song")
public class SongController {

    private static final Logger log = Logger.getLogger(SongController.class);

    @Autowired
    SongRepository songRepository;

    @Autowired
    ArtistRepository artistRepository;

    @RequestMapping("/select")
    public String selectArtist(Model model) {
        return "rmmusic/selectSongForm";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    protected String editSong(@Valid @ModelAttribute("song") Song song,
                              HttpServletRequest request,
                              BindingResult br,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        //TODO: Save song
        List<Artist> artists = artistRepository.findByName(song.getArtist().getName());
        if ( artists.size()!=1) {
            redirectAttributes.addFlashAttribute("error", "Invalid artist. Song was NOT updated");
            String referer = request.getHeader("referer");
            String origin = request.getHeader("origin");
            String redirect = referer.replace(origin, "");

            log.info("Redirecting to redirect:"+redirect);

            return "redirect:"+ redirect;
        }

        song.setArtist(artists.get(0));
        song.setTimestamp(new Date());
        log.info("Got song="+song);

        songRepository.save(song);

        String referer = request.getHeader("referer");
        if ( referer.indexOf("/recording/edit") > -1) {
            // Redirecting to that very edit form
            redirectAttributes.addFlashAttribute("success", "Successfully updated song " + song.getArtist().getName() + " - " + song.getTitle());

            String origin = request.getHeader("origin");
            String redirect = referer.replace(origin, "");

            log.info("Redirecting to redirect:"+redirect);

            return "redirect:"+ redirect;
        }



        return null;
    }
}
