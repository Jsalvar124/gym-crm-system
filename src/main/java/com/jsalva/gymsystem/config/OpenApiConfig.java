package com.jsalva.gymsystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ComponentScan(basePackages = {"org.springdoc"})
public class OpenApiConfig {
    @Bean
    public OpenAPI gymApi() {
        return new OpenAPI()
                .openapi("3.0.1") // Explicitly set OpenAPI version
                .info(new Info()
                        .title("Gym CRM System API")
                        .description("API documentation for Gym System")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Julián Salvá")
                                .url("https://github.com/Jsalvar124/")
                                .email("jsalvar124@gmail.com")));
    }
}
