package de.christophlorenz.rmmusic.web.controller;

import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.model.Song;
import de.christophlorenz.rmmusic.persistence.jpa2.ArtistRepository;
import de.christophlorenz.rmmusic.persistence.jpa2.SongRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editSongById(@PathVariable(value = "id") long id,
                               HttpServletRequest request,
                               Model model) {
        Song song = songRepository.getOne(id);

        model.addAttribute("redirect", request.getHeader("referer"));
        model.addAttribute("song", song);
        model.addAttribute("show_delete_button", 1);
        return "rmmusic/editSongForm";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editSongByArtistAndTitle(@RequestParam(value = "artist", required = true ) final String artist,
                                           @RequestParam(value = "title", required = false ) final String title,
                                           @RequestParam(value = "exact", required = false) final String exact,
                                           RedirectAttributes redirectAttributes,
                                           Model model) {
        List<Song> songs;
        if ( "on".equals(exact)) {
            List<Artist> artists = artistRepository.findByName(artist);
            if ( artists.size()!=1) {
                redirectAttributes.addFlashAttribute("error", "Found not exactly one artist with name="+artist);

                return "redirect:select";
            } else {
                songs = songRepository.findByArtistAndTitle(artists.get(0), title);
            }
        } else {
            songs = songRepository.findByArtistNameIgnoreCaseStartingWithAndTitleIgnoreCaseStartingWithOrderByArtistAscTitleAsc(artist, title);
        }

        if ( songs.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Found no song for "+artist+"-"+title);

            return "redirect:select";
        }
        if ( songs.size()==1) {
            model.addAttribute("song", songs.get(0));
            return "rmmusic/editSongForm";
        } else {
            model.addAttribute("songs", songs);
            return "rmmusic/songsList";
        }


    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    protected String editSong(@Valid @ModelAttribute("song") Song song,
                              @RequestParam(value = "redirect", required = false) String redirect,
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
            redirect = referer.replace(origin, "");

            log.info("Redirecting to redirect:"+redirect);

            return "redirect:"+ redirect;
        }

        song.setArtist(artists.get(0));
        song.setTimestamp(new Date());
        log.info("Got song="+song);

        songRepository.save(song);

        if (StringUtils.isBlank(redirect)) {
            String referer = request.getHeader("referer");
            String origin = request.getHeader("origin");
            redirect = referer.replace(origin, "");
        }

        log.info("Redirect="+redirect);

        // Redirecting to that very edit form
        redirectAttributes.addFlashAttribute("success", "Successfully updated song " + song.getArtist().getName() + " - " + song.getTitle());
        log.info("Redirecting to redirect:"+redirect);

        return "redirect:"+ redirect;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteSong(@Valid @RequestParam("id") Long id,
                               RedirectAttributes redirectAttributes) {
        log.info("Removing song " + id);

        Song song = songRepository.getOne(id);
        if ( song!=null ) {
            try {
                songRepository.delete(song);
                redirectAttributes.addFlashAttribute("success", "Successfully removed song " + song.getArtist().getName()+" - " + song.getTitle() + " with ID " + id);
                return "redirect:select";
            } catch (Exception e) {
                log.error("Cannot delete song with id=" + id + ": ", e);
            }
        }

        redirectAttributes.addFlashAttribute("error", "Could not remove song with id "+id);
        return "redirect:select";
    }
}
