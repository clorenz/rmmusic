package de.christophlorenz.rmmusic.web;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * Created by clorenz on 20.05.15.
 */
@Configuration
public class AppConfig {

    private static final Logger log = LogManager.getLogger(AppConfig.class);

    static {
        log.info("AppConfig");
    }

    /*

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(){
        return new MyCustomizer();
    }


    private static class MyCustomizer implements EmbeddedServletContainerCustomizer {

        @Override
        public void customize(ConfigurableEmbeddedServletContainer container) {
            container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/rmmusic/error/500"));
        }

    }

     */
}
