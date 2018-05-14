package com.objectcomputing.webflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author Brandon Sanders [brandon@alicorn.io]
 */
@Configuration
public class HelloRouter {

    @Bean
    public RouterFunction<ServerResponse> routeHelloWorld(HelloController controller) {

        return RouterFunctions.route(RequestPredicates.POST("/hello")
                                             .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), controller::hello);
    }
}
