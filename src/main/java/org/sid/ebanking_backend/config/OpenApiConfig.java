package org.sid.ebanking_backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 3 / Swagger configuration.
 *
 * The instructor demos Swagger in Part 2:
 *   - Import the /v3/api-docs URL into Postman to auto-generate a collection.
 *   - Browse the Swagger UI at /swagger-ui/index.html to test endpoints.
 *
 * spring-doc-openapi auto-generates the spec; we just add metadata here.
 *
 * Add the following dependency to pom.xml to enable the Swagger UI:
 *   <dependency>
 *     <groupId>org.springdoc</groupId>
 *     <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
 *     <version>2.3.0</version>
 *   </dependency>
 */
@OpenAPIDefinition(
    info = @Info(
        title       = "Digital Banking API",
        version     = "1.0",
        description = "REST API for the Digital Banking Spring + Angular use case",
        contact     = @Contact(name = "ENSET", email = "contact@enset.ma")
    ),
    servers = @Server(url = "http://localhost:8085", description = "Local development server")
)
@Configuration
public class OpenApiConfig {
    // springdoc-openapi auto-scans the controllers; no extra beans required.
}
