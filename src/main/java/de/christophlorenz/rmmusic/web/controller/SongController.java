package de.christophlorenz.rmmusic.web.controller;

import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.model.Medium;
import de.christophlorenz.rmmusic.model.Recording;
import de.christophlorenz.rmmusic.model.Song;
import de.christophlorenz.rmmusic.web.model.SongWithQuality;
import de.christophlorenz.rmmusic.persistence.jpa2.ArtistRepository;
import de.christophlorenz.rmmusic.persistence.jpa2.MediumRepository;
import de.christophlorenz.rmmusic.persistence.jpa2.RecordingRepository;
import de.christophlorenz.rmmusic.persistence.jpa2.SongRepository;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
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

    @Autowired
    MediumRepository mediumRepository;

    @Autowired
    RecordingRepository recordingRepository;

    @RequestMapping(value="/select", method = RequestMethod.GET)
    public String selectArtist(
            @RequestParam(value = "medium_id", required = false) final Long mediumId,
            @RequestParam(value = "side", required = false) final String side,
            @RequestParam(value = "track", required = false) final Integer track,
            @RequestParam(value = "counter", required = false) final String counter,
            Model model) {

        if ( mediumId!=null ) {
            model.addAttribute("medium_id", mediumId);
            Medium medium = mediumRepository.getOne(mediumId);
            model.addAttribute("medium_name", medium.getTypeCode()+" "+medium.getCode());
            if ( medium.getArtist()!=null) {
                model.addAttribute("artist", medium.getArtist().getName());
            }
        }
        if ( side!=null ) { model.addAttribute("side", side); }
        if ( track!=null ) { model.addAttribute("track", track); }
        if ( counter!=null) { model.addAttribute("counter", counter); }
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
        setRecordings(song, model);
        return "rmmusic/editSongForm";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editSongByArtistAndTitle(@RequestParam(value = "artist", required = true ) final String artist,
                                           @RequestParam(value = "title", required = false ) final String title,
                                           @RequestParam(value = "exact", required = false) final String exact,
                                           @RequestParam(value = "medium_id", required = false) final Long mediumId,
                                           @RequestParam(value = "side", required = false) final String side,
                                           @RequestParam(value = "track", required = false) final Integer track,
                                           @RequestParam(value = "counter", required = false) final String counter,
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

        Medium medium=null;
        // If you have to forward to the recording screen, set the following fields:
        if ( mediumId!=null ) {
            model.addAttribute("medium_id", mediumId);
            medium = mediumRepository.getOne(mediumId);
            model.addAttribute("medium_name", medium.getTypeCode()+" "+medium.getCode());
        }
        if ( side != null ) {
            model.addAttribute("side", side);
        }
        if ( track != null ) {
            model.addAttribute("track", track);
        }
        if ( counter != null ) {
            model.addAttribute("counter", counter);
        }


        if ( songs.isEmpty()) {
            log.info("New song!");
            model.addAttribute("editsong", true);
            Song song = new Song();
            if ( !StringUtils.isBlank(artist)) {
                song.setArtist(artistRepository.findByName(artist).get(0));
            }
            if ( !StringUtils.isBlank(title)) {
                song.setTitle(title);
            }
            if ( medium!=null ) {
                song.setYear(medium.getpYear());
                if ( medium.getType()==Medium.LP || medium.getType()==Medium.CD) {
                    song.setRelease("LP");
                } else if ( medium.getType()==Medium.SINGLE) {
                    song.setRelease("S/"+side);
                }
            }
            model.addAttribute("song", song);

            return "rmmusic/editSongForm";
        }
        if ( songs.size()==1) {
            model.addAttribute("song", songs.get(0));
            setRecordings(songs.get(0), model);
            if ( medium!=null) {
                String forward="forward:../recording/new?medium_id="+mediumId+"&song_id="+songs.get(0).getId();
                if ( side!=null ) {
                    forward += "&side="+side;
                }
                if ( track!=null ) {
                    forward += "&track="+track;
                }
                if ( counter!=null ) {
                    forward += "&counter="+counter;
                }
                log.info("forward="+forward);
                return forward;
            } else {
                return "rmmusic/editSongForm";
            }
        } else {
            List<SongWithQuality> songWithQualities = new ArrayList<SongWithQuality>();
            for ( Song song : songs) {
                SongWithQuality swq = new SongWithQuality(song);
                List<Recording> recordings = recordingRepository.findBySong(song);
                int bestQuality=-1;
                String bestTypeCode=null;
                for ( Recording recording : recordings ) {
                    if ( recording.getQuality() > bestQuality) {
                        bestQuality = recording.getQuality();
                        if ( bestTypeCode==null || "C".equals(bestTypeCode) || "R".equals(bestTypeCode) || "V".equals(bestTypeCode)) {
                            bestTypeCode = recording.getMedium().getTypeCode();
                        } else if ( "S".equals(bestTypeCode) && recording.getMedium().getType() > Medium.SINGLE) {
                            bestTypeCode = recording.getMedium().getTypeCode();
                        } else if ( "L".equals(bestTypeCode) && recording.getMedium().getType() > Medium.LP) {
                            bestTypeCode = recording.getMedium().getTypeCode();
                        } else if ( recording.getMedium().getType() == Medium.CD) {
                            bestTypeCode = "D";
                        }
                    }
                }
                swq.setMediumCode(bestTypeCode);
                swq.setQuality(bestQuality);
                songWithQualities.add(swq);
            }
            model.addAttribute("songs", songWithQualities);
            return "rmmusic/songsList";
        }


    }

    private void setRecordings(Song song, Model model) {
        List<Recording> recordings = recordingRepository.findBySong(song);
        if ( !recordings.isEmpty()) {
            model.addAttribute("recordings", recordings);
            log.info("Found recordings="+recordings);
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    protected String editSong(@Valid @ModelAttribute("song") Song song,
                              @RequestParam(value = "medium_id", required = false) final String mediumId,
                              @RequestParam(value = "side", required = false) final String side,
                              @RequestParam(value = "track", required = false) final Integer track,
                              @RequestParam(value = "counter", required = false) final String counter,
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

        song = songRepository.save(song);

        if ( mediumId!=null ) {
            redirect = "../recording/new?song_id="+song.getId()+"&medium_id="+mediumId;
            if ( !StringUtils.isBlank(side)) {
                redirect += "&side="+side;
            }
            if ( track!=null) {
                redirect += "&track="+track;
            }
            if ( !StringUtils.isBlank(counter)) {
                redirect += "&counter="+counter;
            }
        }

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
