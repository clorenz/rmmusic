package de.christophlorenz.rmmusic.web.controller;

import com.google.common.annotations.VisibleForTesting;
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
    private static final String SIX_BLANKS = "      ";

    @Autowired
    MediumRepository mediumRepository;

    @Autowired
    ArtistRepository artistRepository;

    @VisibleForTesting
    protected void setMediumRepository(MediumRepository mediumRepository) {
        this.mediumRepository = mediumRepository;
    }

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


    // TODO: How to get the value of the submit attribute??
    @RequestMapping(value = "/single/edit", method = RequestMethod.POST)
    public String editSingle(@Valid @ModelAttribute("medium") Medium medium,
                             BindingResult br,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        boolean created=false;

        medium.setTimestamp(new Date());

        log.info("Medium="+medium);

        if ( br.hasErrors()) {
            log.info("Errors="+br.toString());
            return "rmmusic/editSingleForm";
        }

        if ( medium.getId()==0)
            created=true;

        medium = mediumRepository.save(medium);
        redirectAttributes.addFlashAttribute("success", "Successfully "+(created?"created":"saved")+" medium S "+medium.getCode());

        return "redirect:select";
    }

    private String editFirstSingleInList(List<Medium> media, Model model) {
        model.addAttribute("medium", media.get(0));
        return "rmmusic/editSingleForm";
    }

    protected String editNewSingle(Model model, Artist artist, String title) {
        Medium single = new Medium();
        single.setType(Medium.SINGLE);
        single.setArtist(artist);
        single.setTitle(title);
        single.setCode(calculateCode(Medium.SINGLE,artist));

        model.addAttribute("medium", single);
        return "rmmusic/editSingleForm";
    }


    @RequestMapping(value = "/single/delete", method = RequestMethod.POST)
    public String deleteArtist(@Valid @RequestParam("id") Long id,
                               RedirectAttributes redirectAttributes) {
        log.info("Removing single " + id);

        Medium medium = mediumRepository.getOne(id);
        if ( medium!=null ) {
            try {
                mediumRepository.delete(medium);
                redirectAttributes.addFlashAttribute("success", "Successfully removed single with code S " + medium.getCode());
                return "redirect:select";
            } catch (Exception e) {
                log.error("Cannot delete medium with id=" + id + ": ", e);
            }
        }

        redirectAttributes.addFlashAttribute("error", "Could not remove medium with id "+id);
        return "redirect:select";
    }


    protected String calculateCode(int mediumType, Artist artist) {
        // Check, if there are already media from this artist. If yes, use their code prefix and incrememnt the number
        if ( artist!=null ) {
            String artistName = artist.getName();
            List<Medium> mediaOfArtist = mediumRepository.findByTypeAndArtist(mediumType, artistName);
            if ( mediaOfArtist==null || mediaOfArtist.isEmpty()) {
                // On this very medium, we have no codes yet, but what on the other ones?
                mediaOfArtist = mediumRepository.findByArtist(artistName);

                // Still nothing found? Create a new one!
                if ( mediaOfArtist==null || mediaOfArtist.isEmpty())
                    return calculateShortenedArtistNameWithOne(artistName);
                else {
                    Collections.sort(mediaOfArtist, new MediaCodeComparator());
                    String lastMedium = mediaOfArtist.get(mediaOfArtist.size()-1).getCode();

                    String lastMediumDigits = lastMedium.replaceAll("\\D", "");

                    String newCode = lastMedium.substring(0,lastMedium.length()-lastMediumDigits.length()-1);
                    return (newCode + SIX_BLANKS).substring(0,6)+" 1";
                }
            }

            // Sort according to their suffix
            Collections.sort(mediaOfArtist, new MediaCodeComparator());
            String lastMedium = mediaOfArtist.get(mediaOfArtist.size()-1).getCode();
            // Extract number
            int lastMediumNumber = Integer.parseInt(lastMedium.replaceAll("\\D", ""));
            int nextMediumNumber = (lastMediumNumber+1);

            String newCode = lastMedium.replace(""+lastMediumNumber, ""+nextMediumNumber);

            if ( newCode.length()>lastMedium.length()) {
                newCode = lastMedium.substring(0,lastMedium.length()-(""+nextMediumNumber).length())+nextMediumNumber;
            }

            return newCode;
        }
        return null;
    }

    private String calculateShortenedArtistNameWithOne(String artistName) {
        String ret="";
        String newCode="";

        String[] artistParts = artistName.replaceAll("\\s", "").split(",");
        int length = (int) Math.floor(6 / Math.min(6, artistParts.length));
        String[] newParts = new String[artistParts.length];

        for (int partNo = 0; partNo < Math.min(6, artistParts.length); partNo++) {
            ret += artistParts[partNo].substring(0, Math.min(length, artistParts[partNo].length()));
        }

        ret += SIX_BLANKS;
        newCode = ret.substring(0, 6);

        if ( mediumRepository.findByCodeStartsWith(newCode).isEmpty())
            return newCode+" 1";
        else
            return null;
    }
}
