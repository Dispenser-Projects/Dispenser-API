package fr.theogiraudet.dispenser_api.converter;

import fr.theogiraudet.dispenser_api.domain.VersionType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class VersionTypeConverter implements Converter<String, VersionType> {

    @Override
    public VersionType convert(String s) {
        for(var type: VersionType.values())
            if(type.name().equalsIgnoreCase(s))
                return type;
        return null;
    }

}
