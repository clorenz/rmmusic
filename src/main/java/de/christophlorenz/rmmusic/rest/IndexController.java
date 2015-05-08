package de.christophlorenz.rmmusic.rest;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by clorenz on 04.05.15.
 */
@RestController
public class IndexController implements ErrorController {

    @RequestMapping(value="/error")
    public String error() {
        return "Error handling";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
