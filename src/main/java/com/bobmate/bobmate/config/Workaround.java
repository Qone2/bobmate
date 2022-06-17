package com.bobmate.bobmate.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.stereotype.Component;
import springfox.documentation.oas.web.OpenApiTransformationContext;
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter;
import springfox.documentation.spi.DocumentationType;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * https://velog.io/@yulhee741/Swagger-servers-url-%EC%B6%94%EA%B0%80%ED%95%98%EA%B8%B0-https-%EC%B6%94%EA%B0%80
 */
//@Component
public class Workaround implements WebMvcOpenApiTransformationFilter {
    @Override
    public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> context) {
        OpenAPI openAPI = context.getSpecification();
        Server localServer = new Server();
        localServer.setDescription("local");
        localServer.setUrl("http://localhost:5050");

        Server cloudServer = new Server();
        cloudServer.setDescription("cloud");
        cloudServer.setUrl("https://bobmate.qone2.com");

        openAPI.setServers(Arrays.asList(cloudServer, localServer));
        return openAPI;
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return delimiter.equals(DocumentationType.OAS_30);
    }
}
