package de.christophlorenz.rmmusic.rest;

import de.christophlorenz.rmmusic.model.TagMediumId;
import de.christophlorenz.rmmusic.persistence.jpa2.TagMediumIdConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

/**
 * Created by clorenz on 13.05.15.
 */
@Configuration
public class RmmusicWebConfiguration extends RepositoryRestMvcConfiguration{

    @Bean
    public TagMediumIdConverter tagMediumIdConverter() {
        return new TagMediumIdConverter();
    }

    @Override
    protected void configureConversionService(ConfigurableConversionService conversionService) {
        conversionService.addConverter(String.class, TagMediumId.class, tagMediumIdConverter());
    }
}
