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
public abstract class MediumController {

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

    protected abstract String getSelectFormTemplate();

    protected abstract String getEditFormTemplate();

    protected abstract String getListFormTemplate();

    protected abstract String getMediumTypeName();

    protected abstract String getMediumTypeAbbreviation();

    protected abstract int getMediumType();

    @RequestMapping("/select")
    public String selectMedium(Model model) {
        return getSelectFormTemplate();
    }


    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    protected String editMedium(final String code, final String artist, final String title, final String exact,
                                final Model model) {

        log.info("Type="+getMediumType()+", code="+code+", artist="+artist+", title="+title+", exact="+exact);

        // If no parameter was set, throw an error
        if (StringUtils.isBlank(code) && StringUtils.isBlank(artist) && StringUtils.isBlank(title)) {
            if ( getMediumType() >= Medium.LP) {
                model.addAttribute("error", "Please fill out at least one field");
                return getSelectFormTemplate();
            } else {
                // For Audio tapes, video tapes, minidiscs and cdroms, we increment the code, so all fields can be empty here
                return editNewMedium(model, null, null, getMediumType(), getEditFormTemplate());
            }
        }

        // If "exact" was set, and no code was given but not both fields, artist and title, are set, throw an error
        if ( "on".equals(exact) && StringUtils.isBlank(code) && ( StringUtils.isBlank(artist) || StringUtils.isBlank(title)) ) {
            model.addAttribute("error", "If 'exact' is selected, you have to fill out both, artist and title or the code field");
            return getSelectFormTemplate();
        }

        if ( !StringUtils.isBlank(code)) {
            Medium medium = mediumRepository.findByTypeAndCode(getMediumType(), code);
            model.addAttribute("medium", medium);
            return getEditFormTemplate();
        } else {
            List<Medium> media;
            if ( "on".equals(exact)) {
                media = mediumRepository.findByTypeAndArtistAndTitleExact(getMediumType(), artist, title);
            } else {
                media = mediumRepository.findByTypeAndArtistAndTitle(getMediumType(), artist, title);
            }

            if ( media.isEmpty()) {
                // Check, if artist is valid
                List<Artist> artists = artistRepository.findByName(artist);
                if ( artists.size()==1) {
                    return editNewMedium(model, artists.get(0), title, getMediumType(), getEditFormTemplate());
                } else {
                    model.addAttribute("error", "Invalid or unknown artist '"+artist+"'");
                    return getSelectFormTemplate();
                }
            } else if ( media.size()==1) {
                return editFirstMediumInList(media, model, getEditFormTemplate());
            } else {
                Collections.sort(media, new MediaCodeComparator());
                model.addAttribute("media", media);
                return getListFormTemplate();
            }

        }
    }


    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    protected String editMediumById(@PathVariable(value="id") Long id,
                                 Model model) {
        try {
            Medium medium = mediumRepository.getOne(id);
            log.debug("Medium=" + medium);
            model.addAttribute("medium", medium);
            return getEditFormTemplate();
        } catch ( Exception e) {
            log.warn("No medium with id="+id+" found!");
            model.addAttribute("error", "No medium found with id="+id);
            return "forward:../select";
        }
    }


    // TODO Ugly, since this should be a GET method, not POST!
    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "editsongs")
    protected String editMediumReturnToEditSongs(@Valid @ModelAttribute("medium") Medium medium,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        return "redirect:../../songsOnMedium/"+medium.getId();
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "sticker")
    protected String editMediumReturnToCreateSticker(@Valid @ModelAttribute("medium") Medium medium,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        log.info("Forwarding to ../../sticker/create");
        return "forward:../../sticker/create";
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    protected String editMedium(@Valid @ModelAttribute("medium") Medium medium,
                             BindingResult br,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        boolean created=false;

        medium.setTimestamp(new Date());
        if ( StringUtils.isBlank(medium.getArtist().getName())) {
            medium.setArtist(null);
        } else {
            List<Artist> artists = artistRepository.findByName(medium.getArtist().getName());
            if ( artists!=null && artists.size()==1) {
                log.info("Changing artist to "+artists.get(0));
                medium.setArtist(artists.get(0));
            }
        }

        log.info("Medium="+medium);

        if ( br.hasErrors()) {
            log.info("Errors="+br.toString());
            return getEditFormTemplate();
        }

        if ( medium.getId()==0)
            created=true;

        medium = mediumRepository.save(medium);
        redirectAttributes.addFlashAttribute("success", "Successfully "+(created?"created":"saved")+" medium "+getMediumTypeAbbreviation()+" "+medium.getCode());

        if ( created ) {
            return "redirect:editSongs?medium_id=" + medium.getId();
        } else {
            return "redirect:select";
        }
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    protected String deleteMedium(@Valid @RequestParam("id") Long id,
                                  RedirectAttributes redirectAttributes) {
        log.info("Removing " + getMediumTypeName()+" " + id);

        Medium medium = mediumRepository.getOne(id);
        if ( medium!=null ) {
            try {
                mediumRepository.delete(medium);
                redirectAttributes.addFlashAttribute("success", "Successfully removed "+ getMediumTypeName()+" with code "
                        +getMediumTypeAbbreviation()+" "+ medium.getCode());
                return "redirect:select";
            } catch (Exception e) {
                log.error("Cannot delete medium with id=" + id + ": ", e);
            }
        }

        redirectAttributes.addFlashAttribute("error", "Could not remove "+ getMediumTypeName()+" with id "+id);
        return "redirect:select";
    }

    private String editFirstMediumInList(List<Medium> media, Model model, String viewEdit) {
        model.addAttribute("medium", media.get(0));
        return viewEdit;
    }

    protected String editNewMedium(Model model, Artist artist, String title, int type, String editFormPath) {
        Medium medium = new Medium();
        medium.setType(type);
        medium.setArtist(artist);
        medium.setTitle(title);
        medium.setCode(calculateCode(type,artist));

        model.addAttribute("medium", medium);
        return editFormPath;
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
        } else {
            // Retrieve highest number from this very medium and increment it afterwards
            List<Medium> mediaOfType = mediumRepository.findByType(mediumType);
            if ( mediaOfType.isEmpty()) {
                return "1";
            }
            Collections.sort(mediaOfType, new MediaCodeComparator());
            String lastMedium = mediaOfType.get(mediaOfType.size()-1).getCode();
            int lastMediumNumber = Integer.parseInt(lastMedium.replaceAll("\\D", ""));
            return ""+(lastMediumNumber+1);
        }
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
