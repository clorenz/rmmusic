package de.christophlorenz.rmmusic.web.controller;

import com.google.common.annotations.VisibleForTesting;
import de.christophlorenz.rmmusic.model.Medium;
import de.christophlorenz.rmmusic.model.Recording;
import de.christophlorenz.rmmusic.model.Song;
import de.christophlorenz.rmmusic.persistence.jpa2.MediumRepository;
import de.christophlorenz.rmmusic.persistence.jpa2.RecordingRepository;
import de.christophlorenz.rmmusic.persistence.jpa2.SongRepository;
import de.christophlorenz.rmmusic.web.model.RecordingsCounterComparator;
import de.christophlorenz.rmmusic.web.model.RecordingsSideAndTrackComparator;
import de.christophlorenz.rmmusic.web.model.RecordingsSideComparator;
import de.christophlorenz.rmmusic.web.model.RecordingsTrackComparator;
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
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by clorenz on 28.05.15.
 */
@Controller
@RequestMapping("/rmmusic/recording")
public class RecordingController {

    private static final Logger log = Logger.getLogger(RecordingController.class);

    @Autowired
    RecordingRepository recordingRepository;

    @Autowired
    MediumRepository mediumRepository;

    @VisibleForTesting
    public void setMediumRepository(MediumRepository mediumRepository) {
        this.mediumRepository = mediumRepository;
    }

    @VisibleForTesting
    public void setRecordingRepository(RecordingRepository recordingRepository) {
        this.recordingRepository = recordingRepository;
    }

    @Autowired
    SongRepository songRepository;

    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");

    @RequestMapping(value = "/", method = RequestMethod.GET)
    protected String editMediumById(@RequestParam(value = "medium") Long mediumId, Model model) {

        List<Recording> recordings = recordingRepository.findByMediumId(mediumId);
        model.addAttribute("recordings", recordings);
        if ( !recordings.isEmpty()) {
            model.addAttribute("mediumcodeprefix", calculateMediumCodePrefix(recordings.get(0).getMedium().getType()));
        }

        Medium medium = mediumRepository.getOne(mediumId);
        model.addAttribute("mediumid", medium.getId());

        String artist = ( medium.getArtist()!=null ? medium.getArtist().getPrint() : null);
        String title = medium.getTitle();
        model.addAttribute("headline", (artist!=null ? (artist + " - ") : "") + (title!=null ? title : ""));
        model.addAttribute("displayTimeFormatter", sdf);

        return "rmmusic/recordingsList";
    }

    @RequestMapping(value="/new", method = RequestMethod.GET)
    protected String addNewRecording(@RequestParam(value = "medium_id", required = false) final Long mediumId,
                                     @RequestParam(value = "song_id") long songId,
                                     @RequestParam(value = "side", required = false) String side,
                                     @RequestParam(value = "track", required = false) final Integer track,
                                     @RequestParam(value = "counter", required = false) final String counter,
                                     Model model) {

        if ( side!=null && side.indexOf(",")>-1 ) {
            // A bug in spring probably, for some reasons, the "side" parameter appears twice, comma separarated!
            side = side.split(",")[0];
        }
        log.info("Side="+side);

        Song song = songRepository.getOne(songId);
        Recording recording = new Recording();
        recording.setSong(song);
        recording.setRecordingYear(song.getYear());

        if ( mediumId != null ) {
            Medium medium = mediumRepository.getOne(mediumId);
            model.addAttribute("medium_id", mediumId);
            recording.setMedium(medium);
            if ( !StringUtils.isBlank(medium.getDigital())) {
                recording.setDigital(medium.getDigital());
            }
            model.addAttribute("display_side", medium.hasSides());
            model.addAttribute("display_track", medium.hasTracks());
            model.addAttribute("display_counter", medium.hasCounter());
            String mediumcode = calculateMediumCodePrefix(recording.getMedium().getType()) + " " + recording.getMedium().getCode();
            model.addAttribute("mediumcode", mediumcode);
        } else {
            model.addAttribute("display_side", true);
            model.addAttribute("display_track", true);
            model.addAttribute("display_counter", true);
        }

        if (side != null) {
            recording.setSide(side);
            model.addAttribute("side",side);
        }
        if (track != null) {
            recording.setTrack(track);
            model.addAttribute("track", track);
        }
        if (counter != null) {
            recording.setCounter(counter);
            model.addAttribute("counter", counter);
        }

        model.addAttribute("recording", recording);
        model.addAttribute("song", song);
        model.addAttribute("editsong", false);

        return "rmmusic/editRecordingForm";
    }

    @RequestMapping(value="/", method = RequestMethod.POST, params = "back")
    protected String backToMedium(@Valid @ModelAttribute("mediumid") long mediumId,
                                          BindingResult br,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {
        Medium medium = mediumRepository.getOne(mediumId);
        return "redirect:../medium/"+ MediumController.PATH.get(medium.getType())+"/edit/"+mediumId;
    }

    @RequestMapping(value="/", method = RequestMethod.POST, params = "add")
    protected String addRecordingToMedium(@Valid @ModelAttribute("mediumid") long mediumId,
                                          BindingResult br,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {
        log.info("Adding new song to "+mediumId);

        String side="";
        Integer track=0;
        String counter="";

        Medium medium = mediumRepository.getOne(mediumId);

        switch(medium.getType()) {
            case Medium.AUDIO_TAPE:
                counter = incrementCounter(mediumId);
                break;
            case Medium.VIDEO_TAPE:
                counter = incrementCounter(mediumId);
                break;
            case Medium.ROM:
                track = incrementTrack(mediumId);
                break;
            case Medium.SINGLE:
                side = incrementSide(mediumId);
                break;
            case Medium.LP:
                String[] sideAndTrack = incrementSideAndTrack(mediumId);
                side = sideAndTrack[0];
                track = Integer.parseInt(sideAndTrack[1]);
                break;
            case Medium.CD:
                track = incrementTrack(mediumId);
                break;
            default:
        }

        // Select song, pass-through mediumId and next position on medium
        String redirect = "redirect:../song/select?medium_id=" + medium.getId() + "&side=" + side + "&track=" + track + "&counter=" + counter;
        log.info("Redirecing to "+redirect);
        return redirect;
    }


    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    protected String editRecordingById(@PathVariable(value = "id") Long recordingId, Model model,
                                       RedirectAttributes redirectAttributes) {

        try {
            Recording recording = recordingRepository.getOne(recordingId);
            log.info("Found recording="+recording);
            model.addAttribute("recording", recording);

            Song song = recording.getSong();
            model.addAttribute("song", song);

            String mediumcode = calculateMediumCodePrefix(recording.getMedium().getType()) + " " + recording.getMedium().getCode();
            model.addAttribute("mediumcode", mediumcode);
            model.addAttribute("display_side", recording.getMedium().hasSides());
            model.addAttribute("display_track", recording.getMedium().hasTracks());
            model.addAttribute("display_counter", recording.getMedium().hasCounter());

            log.info("Mediumcode="+mediumcode);

            return "rmmusic/editRecordingForm";
        } catch ( Exception e) {
            log.error("Cannot find recording with id="+recordingId+": ",e);
        }

        redirectAttributes.addFlashAttribute("error", "Recording with id=" + recordingId + " does not exist!");
        return "redirect:../../";
    }


    @RequestMapping(value="/delete", method = RequestMethod.POST)
    protected String deleteRecording(@Valid @RequestParam("id") Long id,
                                     RedirectAttributes redirectAttributes) {
        Recording recording = recordingRepository.getOne(id);
        log.info("Deleting recording="+recording);
        recordingRepository.delete(recording);

        // Redirect to song
        Medium medium = recording.getMedium();
        redirectAttributes.addFlashAttribute("success", "Removed recording on "+recording.getPosition());
        return "redirect:../song/edit/"+recording.getSong().getId();
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    protected String editRecording(@Valid @ModelAttribute("recording") Recording recording,
                                   @RequestParam(value = "medium_id", required = false) final Long mediumId,
                                   @RequestParam(value = "medium_code", required = false) final String mediumcode,
                                   BindingResult br,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        if ( br.hasErrors()) {
            log.error(br);
            return "rmmusic/artistEdit";
        }

        recording.setTimestamp(new Date());
        if ( "null".equals(recording.getCounter())) {
            recording.setCounter(null);
        }
        if ( "null".equals(recording.getLongplay())) {
            recording.setLongplay(null);
        }

        if ( mediumId == null && !StringUtils.isBlank(mediumcode)) {
            // A new recording from the song screen
            String typeCode = mediumcode.substring(0,1);
            String code = mediumcode.substring(2);

            for (Map.Entry<Integer, String> type : Medium.TYPECODES.entrySet() ) {
                if ( type.getValue().equals(typeCode)) {
                    Medium medium = mediumRepository.findByTypeAndCode(type.getKey(), code);
                    recording.setMedium(medium);
                    log.info("Set Medium="+medium+" to recording="+recording);
                    break;
                }
            }

        }

        log.info("Got recording="+recording);

        recording = recordingRepository.save(recording);

        log.info("mediumId="+mediumId);

        // Redirect...
        if ( mediumId!=null) {
            return "redirect:../recording/?medium="+mediumId;
        } else {
            return "redirect:../song/edit/"+recording.getSong().getId();
        }
    }

    protected String incrementCounter(long mediumId) {
        List<Recording> recordings = recordingRepository.findByMediumId(mediumId);
        if ( !recordings.isEmpty()) {
            Collections.sort(recordings, new RecordingsCounterComparator());
            String latestCounter = recordings.get(recordings.size() - 1).getCounter();
            try {
                int counter = Integer.parseInt(latestCounter.replaceAll("\\D",""));
                return ""+(counter+1);
            } catch ( Exception e) {
                return "";
            }
        }
        return "0";
    }

    protected Integer incrementTrack(long mediumId) {
        List<Recording> recordings = recordingRepository.findByMediumId(mediumId);
        if ( !recordings.isEmpty()) {
            Collections.sort(recordings, new RecordingsTrackComparator());
            Integer latestTrack = recordings.get(recordings.size() - 1).getTrack();
            if (latestTrack != null) {
                return latestTrack + 1;
            }
        }

        return 1;
    }

    protected String incrementSide(long mediumId) {
        List<Recording> recordings = recordingRepository.findByMediumId(mediumId);
        if ( !recordings.isEmpty()) {
            Collections.sort(recordings, new RecordingsSideComparator());
            String latestSide = recordings.get(recordings.size() - 1).getSide();
            if (!StringUtils.isBlank(latestSide)) {
                System.out.println("latestSide="+latestSide);
                byte latestSideOrdinal = (byte) (latestSide.toUpperCase().trim().charAt(0) - 'A');
                System.out.println("ordinal="+latestSideOrdinal);
                return new String(new byte[]{(byte) (latestSideOrdinal + 'A' + 1)});
            }
        }

        return "A";
    }

    protected String[] incrementSideAndTrack(long mediumId) {
        List<Recording> recordings = recordingRepository.findByMediumId(mediumId);
        if ( !recordings.isEmpty()) {
            Collections.sort(recordings, new RecordingsSideAndTrackComparator());
            String latestSide = recordings.get(recordings.size() - 1).getSide();
            Integer latestTrack = recordings.get(recordings.size() - 1).getTrack();
            if (latestTrack != null) {
                if ( StringUtils.isBlank(latestSide)) {
                    latestSide="A";
                }
                return new String[] { latestSide, ""+(latestTrack + 1) };
            } else {
                if ( StringUtils.isBlank(latestSide)) {
                    latestSide="A";
                }
                return new String[] { latestSide, "1" };
            }
        }
        return new String[] { "A", "1" };
    }



    private String calculateMediumCodePrefix(int type) {
        switch (type) {
            case 0: return "C";
            case 1: return "V";
            case 2: return "R";
            case 5: return "L";
            case 6: return "S";
            case 7: return "D";
            default: return "";
        }
    }

}
