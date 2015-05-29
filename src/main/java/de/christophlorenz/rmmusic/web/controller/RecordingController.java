package de.christophlorenz.rmmusic.web.controller;

import de.christophlorenz.rmmusic.model.Medium;
import de.christophlorenz.rmmusic.model.Recording;
import de.christophlorenz.rmmusic.model.Song;
import de.christophlorenz.rmmusic.persistence.jpa2.MediumRepository;
import de.christophlorenz.rmmusic.persistence.jpa2.RecordingRepository;
import de.christophlorenz.rmmusic.persistence.jpa2.SongRepository;
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
import java.util.Date;
import java.util.List;

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
        model.addAttribute("medium", medium);
        model.addAttribute("displayTimeFormatter", sdf);

        return "rmmusic/recordingsList";
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

            log.info("Mediumcode="+mediumcode);

            return "rmmusic/editRecordingForm";
        } catch ( Exception e) {
            log.error("Cannot find recording with id="+recordingId+": ",e);
        }

        redirectAttributes.addFlashAttribute("error", "Recording with id="+recordingId+" does not exist!");
        return "redirect:../../";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    protected String editRecording(@Valid @ModelAttribute("recording") Recording recording,
                                   BindingResult br,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        recording.setTimestamp(new Date());

        log.info("Got recording="+recording);

        return null;
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
