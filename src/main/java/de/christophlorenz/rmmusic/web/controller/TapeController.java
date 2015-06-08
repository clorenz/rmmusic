package de.christophlorenz.rmmusic.web.controller;

import de.christophlorenz.rmmusic.model.Medium;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by clorenz on 28.05.15.
 */
@Controller
@RequestMapping("/rmmusic/medium/tape")
public class TapeController extends MediumController {

    @Override
    protected String getSelectFormTemplate() {
        return "rmmusic/selectTapeForm";
    }

    @Override
    protected String getEditFormTemplate() {
        return "rmmusic/editTapeForm";
    }

    @Override
    protected String getListFormTemplate() {
        return "rmmusic/mediaList";
    }

    @Override
    protected String getMediumTypeName() {
        return "tape";
    }

    @Override
    protected int getMediumType() {
        return Medium.AUDIO_TAPE;
    }
}
