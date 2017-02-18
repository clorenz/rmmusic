package de.christophlorenz.rmmusic.rest;

import de.christophlorenz.rmmusic.persistence.jpa2.ArtistRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by clorenz on 30.04.15.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"de.christophlorenz.rmmusic", "de.christophlorenz.rmmusic.model", "de.christophlorenz.rmmusic.web"})
@EnableJpaRepositories(basePackages = {"de.christophlorenz.rmmusic.persistence.jpa2"})
@ImportResource("classpath:/applicationContext.xml")
@PropertySource(value="classpath:/version.properties")
public class Application {

    private static final Logger log = Logger.getLogger(Application.class);

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        ArtistRepository artistRepository = (ArtistRepository) ctx.getBean(ArtistRepository.class);

        if ( !healthCheck(artistRepository)) {
            System.err.println("Please set up the database according to INSTALL");
            ((ConfigurableApplicationContext)ctx).close();
        }
    }

    private static boolean healthCheck(ArtistRepository artistRepository) {
        try {
            artistRepository.count();
            return true;
        } catch ( Exception e) {
            log.error("Health check for artistRepository failed: ",e);
        }

        return false;
    }
}
