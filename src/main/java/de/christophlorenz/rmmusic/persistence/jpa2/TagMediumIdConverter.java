package de.christophlorenz.rmmusic.persistence.jpa2;

import de.christophlorenz.rmmusic.model.TagMediumId;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by clorenz on 13.05.15.
 */
public class TagMediumIdConverter implements Converter<String, TagMediumId> {

    public TagMediumId convert(String id) {
        String[] parts = id.split("-");
        return new TagMediumId(parts[0],Long.parseLong(parts[1]));
    }

}
