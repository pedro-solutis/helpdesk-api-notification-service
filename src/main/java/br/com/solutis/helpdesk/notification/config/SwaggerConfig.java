package br.com.solutis.helpdesk.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Notification Service API")
                        .version("1.0.0")
                        .description("API responsável pelo gerenciamento e consulta das notificações geradas a partir de eventos do HelpDesk.")
                        .contact(new Contact()
                                .name("Pedro Ferreira")
                                .email("pedro.ferreira@example.com")));
    }
}

