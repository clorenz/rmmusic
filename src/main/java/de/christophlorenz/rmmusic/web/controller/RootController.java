package de.christophlorenz.rmmusic.web.controller;

import de.christophlorenz.rmmusic.persistence.jpa2.ArtistRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by clorenz on 15.05.15.
 */
@Controller
@RequestMapping("/rmmusic")
public class RootController {

    static final Logger log = LogManager.getLogger(RootController.class);

    @Autowired
    ArtistRepository artistRepository;

    private static final String VERSION="0.01";

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                new SimpleDateFormat("dd.MM.yyyy"), false));
    }

    @RequestMapping("/")
    public void index(Model model) {
        model.addAttribute("version",VERSION);
    }

}
