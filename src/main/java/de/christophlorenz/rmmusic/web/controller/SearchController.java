package de.christophlorenz.rmmusic.web.controller;

import de.christophlorenz.rmmusic.model.Medium;
import de.christophlorenz.rmmusic.model.Recording;
import de.christophlorenz.rmmusic.model.Song;
import de.christophlorenz.rmmusic.persistence.jpa2.ArtistRepository;
import de.christophlorenz.rmmusic.persistence.jpa2.RecordingRepository;
import de.christophlorenz.rmmusic.persistence.jpa2.SongRepository;
import de.christophlorenz.rmmusic.web.model.SongWithQuality;
import de.christophlorenz.rmmusic.web.model.SongWithQualityAndTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clorenz on 18.02.17.
 */
@Controller
@RequestMapping("/rmmusic/search")
public class SearchController {

    private static final Logger log = LogManager.getLogger(SearchController.class);

    @Autowired
    RecordingRepository recordingRepository;

    @Autowired
    SongRepository songRepository;

    @RequestMapping(value="/select", method = RequestMethod.GET)
    public String selectSearch(Model model) {
        return "rmmusic/selectSearchForm";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchSongs(@RequestParam(value = "artist", required = false ) final String artist,
                              @RequestParam(value = "title", required = false ) final String title,
                              @RequestParam(value = "authors", required = false) final String authors,
                                           RedirectAttributes redirectAttributes,
                                           Model model) {
        List<Song> songs =
                songRepository.findByArtistNameIgnoreCaseStartingWithAndTitleIgnoreCaseContainingAndAuthorsIgnoreCaseContainingOrderByArtistAscTitleAsc(artist, title, authors);

        List<SongWithQualityAndTime> songWithQualities = new ArrayList<SongWithQualityAndTime>();
        for (Song song : songs) {
            SongWithQualityAndTime swq = new SongWithQualityAndTime(song);
            List<Recording> recordings = recordingRepository.findBySong(song);
            int bestQuality = -1;
            String bestTypeCode = null;
            for (Recording recording : recordings) {
                if (recording.getQuality() >= bestQuality) {
                    bestQuality = recording.getQuality();
                    if (bestTypeCode == null || "C".equals(bestTypeCode) || "R".equals(bestTypeCode) || "V".equals(bestTypeCode)) {
                        bestTypeCode = recording.getMedium().getTypeCode();
                    } else if ("S".equals(bestTypeCode) && recording.getMedium().getType() > Medium.SINGLE) {
                        bestTypeCode = recording.getMedium().getTypeCode();
                    } else if ("L".equals(bestTypeCode) && recording.getMedium().getType() > Medium.LP) {
                        bestTypeCode = recording.getMedium().getTypeCode();
                    } else if (recording.getMedium().getType() == Medium.CD) {
                        bestTypeCode = "D";
                    }
                    if ( recording.getTime() != null && recording.getTime() > swq.getTime()) {
                        swq.setTime(recording.getTime());
                    }

                }
            }
            swq.setMediumCode(bestTypeCode);
            swq.setQuality(bestQuality);
            songWithQualities.add(swq);
        }
        model.addAttribute("songs", songWithQualities);
        model.addAttribute("amount", songWithQualities.size()+" found songs");
        return "rmmusic/searchSongsList";
    }
}
