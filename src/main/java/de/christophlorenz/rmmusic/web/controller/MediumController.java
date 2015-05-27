package de.christophlorenz.rmmusic.web.controller;

import de.christophlorenz.rmmusic.persistence.jpa2.MediumRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by clorenz on 27.05.15.
 */
@Controller
@RequestMapping("/rmmusic/medium")
public class MediumController {

    private static final Logger log = Logger.getLogger(MediumController.class);

    @Autowired
    MediumRepository mediumRepository;

    @RequestMapping("/single/select")
    public String selectSingle(Model model) {
        return "rmmusic/selectSingleForm";
    }
}
