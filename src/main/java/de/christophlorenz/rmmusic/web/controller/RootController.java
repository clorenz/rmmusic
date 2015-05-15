package de.christophlorenz.rmmusic.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by clorenz on 15.05.15.
 */
@Controller
@RequestMapping("/rmmusic")
public class RootController {

    private static final String VERSION="0.01";

    @RequestMapping("/index")
    public void index(Model model) {
        model.addAttribute("version",VERSION);
    }

    @RequestMapping("/artist/select")
    public String selectArtist(Model model) {
        return "rmmusic/artistForm";
    }
}
