package com.tracker.adp.config.app;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class OpenAPIConfig {

    static {
        var schema = new Schema<LocalTime>();
        schema.example(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))).type("string");
        SpringDocUtils.getConfig().replaceWithSchema(LocalTime.class, schema);
    }


    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(
                        new Components().addSecuritySchemes(BEARER_KEY_SECURITY_SCHEME,
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info().title(applicationName));
    }

/*    @Bean
    public GroupedOpenApi actuatorApi() {
        return GroupedOpenApi.builder().group("actuator").pathsToMatch("/actuator/**").build();
    }*/

    @Bean
    public GroupedOpenApi customApi() {
        return GroupedOpenApi.builder().group("api").pathsToMatch("/api/**").build();
    }

    public static final String BEARER_KEY_SECURITY_SCHEME = "Bearer Authentication";
}
