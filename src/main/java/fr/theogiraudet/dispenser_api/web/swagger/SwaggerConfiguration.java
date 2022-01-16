package fr.theogiraudet.dispenser_api.web.swagger;

import fr.theogiraudet.dispenser_api.Utils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SwaggerConfiguration {

    private final String description;

    @Bean
    public OpenAPI customOpenAPI(@Value("${app.version}") String appVersion) {
        return new OpenAPI()
                .info(new Info()
                        .title("Minecraft Resource API")
                        .version(appVersion)
                        .description(description)

                );
    }

    private int putVersionFirst(Tag a, Tag b) {
        if (a.getName().equalsIgnoreCase("version"))
            return -1;
        if (b.getName().equalsIgnoreCase("version"))
            return 1;
        return StringUtils.stripAccents(a.getName()).compareToIgnoreCase(StringUtils.stripAccents(b.getName()));
    }

    public SwaggerConfiguration() {
        final var opt = Utils.readResourceFile("description");
        if (opt.isEmpty())
            throw new NullPointerException("'description' file doesn't exist in resources");
        description = opt.get();
    }
}
