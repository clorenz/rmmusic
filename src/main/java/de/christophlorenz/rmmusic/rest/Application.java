package de.christophlorenz.rmmusic.rest;

import de.christophlorenz.rmmusic.model.Artist;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by clorenz on 30.04.15.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"de.christophlorenz.rmmusic", "de.christophlorenz.rmmusic.model", "de.christophlorenz.rmmusic.web"})
@EnableJpaRepositories(basePackages = {"de.christophlorenz.rmmusic.persistence.jpa2"})
@ImportResource("classpath:/applicationContext.xml")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
