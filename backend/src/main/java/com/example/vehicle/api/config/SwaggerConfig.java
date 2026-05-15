package com.example.vehicle.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Vehicle Inventory API")
            .version("1.0.0")
            .description("API REST para gerenciamento de estoque de veículos. " +
                "Permite cadastro e consulta de marcas, modelos e veículos com filtros avançados.")
            .contact(new Contact()
                .name("Sistema Automotivo"))
            .license(new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
  }
}
