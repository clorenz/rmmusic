package de.christophlorenz.rmmusic.web.controller;

import de.christophlorenz.rmmusic.model.Medium;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by clorenz on 28.05.15.
 */
@Controller
@RequestMapping("/rmmusic/medium/lp")
public class LPController extends MediumController {

    @Override
    protected String getSelectFormTemplate() {
        return "rmmusic/selectLPForm";
    }

    @Override
    protected String getEditFormTemplate() {
        return "rmmusic/editLPForm";
    }

    @Override
    protected String getListFormTemplate() {
        return "rmmusic/mediaList";
    }

    @Override
    protected String getMediumTypeName() {
        return "LP";
    }

    @Override
    protected int getMediumType() {
        return Medium.LP;
    }
}
