package fr.theogiraudet.tp.spring.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SwaggerConfiguration {

    //@Value("${base_url}")
    private String urlBase;

    @Bean
    public OpenAPI customOpenAPI(@Value("${app.version}") String appVersion) {
        return new OpenAPI()
                .info(new Info()
                        .title("DISTIC API")
                        .version(appVersion)
                        .description("Cette API permet de gérer et prendre des rendez-vous médicaux aussi bien pour le médecin que pour la patient. Le **site utilisateur** est disponible en cliquant [ici](" + urlBase + ").")
                );
    }


}
