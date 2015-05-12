package de.christophlorenz.rmmusic.rest;

import de.christophlorenz.rmmusic.model.Artist;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by clorenz on 30.04.15.
 */
@SpringBootApplication
@EnableJpaRepositories
@ComponentScan("de.christophlorenz.rmmusic")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
