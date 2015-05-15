package de.christophlorenz.rmmusic.rest;

import de.christophlorenz.rmmusic.model.TagMediumId;
import de.christophlorenz.rmmusic.persistence.jpa2.TagMediumIdConverter;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 * Created by clorenz on 13.05.15.
 */
@Configuration
public class RmmusicWebConfiguration extends RepositoryRestMvcConfiguration{

    private static final Logger log = Logger.getLogger(RmmusicWebConfiguration.class);

    @Bean
    public TagMediumIdConverter tagMediumIdConverter() {
        return new TagMediumIdConverter();
    }

    @Override
    protected void configureConversionService(ConfigurableConversionService conversionService) {
        conversionService.addConverter(String.class, TagMediumId.class, tagMediumIdConverter());
    }

    @Bean
    public ViewResolver viewResolver() {


        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setSuffix(".html");

        log.info("Using templateResolver="+templateResolver);

        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver);

        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(engine);
        return viewResolver;
    }
}
