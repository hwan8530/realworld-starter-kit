package com.realworld.backend.common.docs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenApi() {
    String jwt = "JWT";
    SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
    Components components = new Components().addSecuritySchemes(jwt,
        new SecurityScheme().name(jwt).type(SecurityScheme.Type.HTTP).scheme("bearer")
            .bearerFormat(jwt));
    return new OpenAPI()
        .info(new Info()
            .title("RealWorld API")
            .version("1.0")
            .description("API Documentation for RealWorld Backend"))
        .addSecurityItem(securityRequirement).components(components);

  }
}
