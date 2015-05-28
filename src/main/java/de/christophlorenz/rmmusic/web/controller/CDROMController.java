package de.christophlorenz.rmmusic.web.controller;

import de.christophlorenz.rmmusic.model.Medium;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by clorenz on 28.05.15.
 */
@Controller
@RequestMapping("/rmmusic/medium/cdrom")
public class CDROMController extends MediumController {

    @Override
    protected String getSelectFormTemplate() {
        return "rmmusic/selectCDROMForm";
    }

    @Override
    protected String getEditFormTemplate() {
        return "rmmusic/editCDROMForm";
    }

    @Override
    protected String getListFormTemplate() {
        return "rmmusic/mediaList";
    }

    @Override
    protected String getMediumTypeName() {
        return "CDROM";
    }

    @Override
    protected String getMediumTypeAbbreviation() {
        return "R";
    }

    @Override
    protected int getMediumType() {
        return Medium.ROM;
    }
}
