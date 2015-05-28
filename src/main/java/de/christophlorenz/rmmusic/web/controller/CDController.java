package de.christophlorenz.rmmusic.web.controller;

import de.christophlorenz.rmmusic.model.Medium;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by clorenz on 28.05.15.
 */
@Controller
@RequestMapping("/rmmusic/medium/cd")
public class CDController extends MediumController {

    @Override
    protected String getSelectFormTemplate() {
        return "rmmusic/selectCDForm";
    }

    @Override
    protected String getEditFormTemplate() {
        return "rmmusic/editCDForm";
    }

    @Override
    protected String getListFormTemplate() {
        return "rmmusic/mediaList";
    }

    @Override
    protected String getMediumTypeName() {
        return "CD";
    }

    @Override
    protected String getMediumTypeAbbreviation() {
        return "D";
    }

    @Override
    protected int getMediumType() {
        return Medium.CD;
    }
}
