package de.christophlorenz.rmmusic.rest;

import de.christophlorenz.rmmusic.model.TagMediumId;
import de.christophlorenz.rmmusic.persistence.jpa2.TagMediumIdConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

/**
 * Created by clorenz on 13.05.15.
 */
@Configuration
public class RmmusicRestConfiguration extends RepositoryRestMvcConfiguration{

    private static final Logger log = LogManager.getLogger(RmmusicRestConfiguration.class);

    public RmmusicRestConfiguration(ApplicationContext context,
        ObjectFactory<ConversionService> conversionService) {
        super(context, conversionService);
    }

    @Bean
    public TagMediumIdConverter tagMediumIdConverter() {
        return new TagMediumIdConverter();
    }


    /*
    @Override
    protected void configureConversionService(ConfigurableConversionService conversionService) {
        conversionService.addConverter(String.class, TagMediumId.class, tagMediumIdConverter());
    }

     */
}
