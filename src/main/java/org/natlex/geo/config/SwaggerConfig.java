package org.natlex.geo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/22/24
 * Time: 3:31 PM
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        String schemeName = "GeoClassify Auth Scheme";
        return new OpenAPI()
                .info(new Info().title("Sections and Geological classes management service"))
                .addSecurityItem(new SecurityRequirement().addList(schemeName))
                .components(new Components().addSecuritySchemes(schemeName, new SecurityScheme()
                        .name(schemeName).type(SecurityScheme.Type.HTTP).scheme("basic")));
    }
}
