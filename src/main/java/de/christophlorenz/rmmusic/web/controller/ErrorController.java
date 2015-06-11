package de.christophlorenz.rmmusic.web.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by clorenz on 11.06.15.
 */
@ControllerAdvice
@RequestMapping("/rmmusic/error")
public class ErrorController {

    private static final Logger log = Logger.getLogger(ErrorController.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView error500(Exception exception) {
        log.error("Error 500: "+exception, exception);

        ModelAndView mav = new ModelAndView();

        // Dig out the initial cause
        Throwable cause = exception.getCause();
        Throwable causeToDisplay=exception;
        while ( cause!=null ) {
            causeToDisplay = cause;
            cause = cause.getCause();
        }

        mav.addObject("error", causeToDisplay);
        mav.setViewName("error");
        return mav;
    }
}
