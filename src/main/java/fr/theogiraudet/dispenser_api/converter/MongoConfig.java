package fr.theogiraudet.dispenser_api.converter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

@Configuration
/**
 * Class configuration for bind converter to MongoDB
 */
class MongoConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(
                Arrays.asList(
                        new OffsetDateTimeReadConverter(),
                        new OffsetDateTimeWriteConverter(),
                        new MinecraftAssetReadConverter(),
                        new MinecraftAssetWriteConverter()
                ));
    }


}
