package fr.theogiraudet.dispenser_api.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@ReadingConverter
class OffsetDateTimeReadConverter implements Converter<Date, OffsetDateTime> {
    @Override
    public OffsetDateTime convert(Date date) {
        return date.toInstant().atOffset(ZoneOffset.UTC);
    }
}
