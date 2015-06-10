package de.christophlorenz.rmmusic.web.controller;

import de.christophlorenz.rmmusic.model.Medium;
import de.christophlorenz.rmmusic.persistence.jpa2.ArtistRepository;
import de.christophlorenz.rmmusic.persistence.jpa2.MediumRepository;
import de.christophlorenz.rmmusic.persistence.jpa2.RecordingRepository;
import de.christophlorenz.rmmusic.persistence.jpa2.SongRepository;
import de.christophlorenz.rmmusic.web.model.MediumStatistics;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by clorenz on 10.06.15.
 *
 * Number of recordings in perfect quality:
 * select count(distinct(s.id)) from song s inner join recording r on s.id=r.song_id and r.quality=7;
 *
 * Best medium of all songs:
 * select r.song_id,max(m.type) as bestmedia from recording r, medium m where m.id=medium_id group by r.song_id;
 * Iterate over that list and count, how many have a bestmedia value >=5
 */
@Controller
@RequestMapping("/rmmusic/statistics")
public class StatisticsController {

    private static final Logger log = Logger.getLogger(StatisticsController.class);

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    SongRepository songRepository;

    @Autowired
    RecordingRepository recordingRepository;

    @Autowired
    MediumRepository mediumRepository;

    @RequestMapping("")
    public String getStatistics(Model model) {
        calculateSongsAndArtistsData(model);
        calculateMediaData(model);
        calculateRecordingsData(model);

        return "/rmmusic/statistics";
    }

    private void calculateRecordingsData(Model model) {
        Map<Integer,Long> recordingQualities = recordingRepository.countRecordingQualities();
        log.info("recordingQualities="+recordingQualities);
        model.addAttribute("recordingqualities", recordingQualities);
    }

    private void calculateMediaData(Model model) {
        Map<Integer,MediumStatistics> mediumStatistics = new HashMap<Integer,MediumStatistics>();

        for ( int type = Medium.AUDIO_TAPE; type <= Medium.CD; type ++) {
            List<Medium> media = mediumRepository.findByType(type);

            if ( !media.isEmpty()) {
                double sumValue = 0;
                long boughtMediaCount=0;
                for (Medium medium : media) {
                    if (medium.getBuyPrice() != null) {
                        sumValue += medium.getBuyPrice();
                        boughtMediaCount++;
                    }
                }
                MediumStatistics stat = new MediumStatistics();
                stat.setAmount((long) media.size());
                stat.setMediumTypeName(Medium.TYPENAMES.get(type) + (media.size() != 1 ? "s" : ""));
                stat.setFormattedSumValue(String.format("EUR %.02f", sumValue));
                if ( boughtMediaCount>0) {
                    stat.setFormattedAvgValue(String.format("EUR %.02f", (sumValue / (double) boughtMediaCount)));
                }
                stat.setBoughtMediaCount(boughtMediaCount);
                mediumStatistics.put(type, stat);
            }
        }
        model.addAttribute("mediastatistics", mediumStatistics);
        log.info("mediastatistics="+mediumStatistics);
    }

    private void calculateSongsAndArtistsData(Model model) {
        long numberOfArtists = artistRepository.count();
        log.info("Number of artists="+numberOfArtists);
        model.addAttribute("artists", numberOfArtists);

        long numberOfSongs = songRepository.count();
        log.info("Number of songs="+numberOfSongs);
        model.addAttribute("songs", numberOfSongs);

        Map<Integer,Long> songsOnMediaType = recordingRepository.countSongsOnMediaType();
        long numberOfSongsOnOriginalMedia = 0;
        for ( int type= Medium.LP ; type <= Medium.CD; type++) {
            if ( songsOnMediaType.get(type)!=null)    {
                numberOfSongsOnOriginalMedia += songsOnMediaType.get(type);
            }
        }
        log.info("Songs on media type="+songsOnMediaType);
        log.info("Number of songs on original media="+numberOfSongsOnOriginalMedia);
        model.addAttribute("songsOnOriginalMedia", numberOfSongsOnOriginalMedia);
        model.addAttribute("songsOnOriginalMediaPercentage", String.format("%.01f%%", 100d*((double)numberOfSongsOnOriginalMedia)/(double)numberOfSongs));

        Map<Integer,Long> songQualities = recordingRepository.countSongQualities();
        long numberOfSongsInBestQuality=(songQualities.get(7)!=null?songQualities.get(7):0);
        log.info("Song qualities="+songQualities);
        log.info("Number of songs in best quality="+numberOfSongsInBestQuality);
        model.addAttribute("songqualities", songQualities);
        model.addAttribute("numberOfSongsInBestQuality", numberOfSongsInBestQuality);
        model.addAttribute("numberOfSongsInBestQualityPercentage", String.format("%.01f%%", 100d*((double)numberOfSongsInBestQuality)/(double)numberOfSongs));

    }
}