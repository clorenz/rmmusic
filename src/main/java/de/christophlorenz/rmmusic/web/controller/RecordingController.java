package de.christophlorenz.rmmusic.web.controller;

import de.christophlorenz.rmmusic.model.Recording;
import de.christophlorenz.rmmusic.persistence.jpa2.RecordingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by clorenz on 28.05.15.
 */
@Controller
@RequestMapping("/rmmusic/recording")
public class RecordingController {

    @Autowired
    RecordingRepository recordingRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    protected String editMediumById(@RequestParam(value = "medium") Long mediumId, Model model) {

        List<Recording> recordings = recordingRepository.findByMediumId(mediumId);

        model.addAttribute("recordings", recordings);
        if ( !recordings.isEmpty()) {
            model.addAttribute("mediumcodeprefix", calculateMediumCodePrefix(recordings.get(0).getMedium().getType()));
        }

        return "rmmusic/recordingsList";
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
