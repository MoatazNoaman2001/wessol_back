package com.wessol.app.core.Config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(

                contact = @Contact(
                        name = "@moataz_noaman",
                        email = "Moataz.Noaman12@gmail.com"
                ),
                description = "OpenAI Docs For Wessol App",
                title = "Wessol OpenAI",
                version = "1.0",
                license =  @License(
                        name = "Wessol Backend licence"
                )

        ),

        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:3000"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "Jwt authentication",
        scheme = "bearer",
        type =  SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenApiConfig {
//    @Bean
//    public OpenAPI baseOpenApi(){
//        return new OpenAPI()
//                .info(new Info().title("Spring Doc").version("1.0.0").description("Spring doc")
//                        .license(new io.swagger.v3.oas.models.info.License().name("Wessol Backend licence"))
//                        .contact(new io.swagger.v3.oas.models.info.Contact().name("@moataz_noaman").email("Moataz.Noaman12@gmail.com")));
//    }
}
