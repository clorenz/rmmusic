package de.christophlorenz.rmmusic.web.controller;

import de.christophlorenz.rmmusic.model.Medium;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by clorenz on 28.05.15.
 */
@Controller
@RequestMapping("/rmmusic/medium/single")
public class SingleController extends MediumController {


    @Override
    protected String getSelectFormTemplate() {
        return "rmmusic/selectSingleForm";
    }

    @Override
    protected String getEditFormTemplate() {
        return "rmmusic/editSingleForm";
    }

    @Override
    protected String getListFormTemplate() {
        return "rmmusic/mediaList";
    }

    @Override
    protected String getMediumTypeName() {
        return "single";
    }

    @Override
    protected int getMediumType() {
        return Medium.SINGLE;
    }
}
