package com.objectcomputing.webflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
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

    @Bean
    public RouterFunction<ServerResponse> routeHello0() {
        return RouterFunctions.route(RequestPredicates.POST("/hello0"), (req) ->
            ServerResponse.ok().body(BodyInserters.fromObject("You said hello to an alternate POST endpoint!")));
    }

    @Bean
    public RouterFunction<ServerResponse> routeHello1() {
        return RouterFunctions.route(RequestPredicates.POST("/hello1"), (req) ->
                ServerResponse.ok().body(BodyInserters.fromObject("You said hello to an alternate POST endpoint!")));
    }

    @Bean
    public RouterFunction<ServerResponse> routeHello2() {
        return RouterFunctions.route(RequestPredicates.POST("/hello2"), (req) ->
                ServerResponse.ok().body(BodyInserters.fromObject("You said hello to an alternate POST endpoint!")));
    }

    @Bean
    public RouterFunction<ServerResponse> routeHello3() {
        return RouterFunctions.route(RequestPredicates.POST("/hello3"), (req) ->
                ServerResponse.ok().body(BodyInserters.fromObject("You said hello to an alternate POST endpoint!")));
    }

    @Bean
    public RouterFunction<ServerResponse> routeHello4() {
        return RouterFunctions.route(RequestPredicates.POST("/hello4"), (req) ->
                ServerResponse.ok().body(BodyInserters.fromObject("You said hello to an alternate POST endpoint!")));
    }

    @Bean
    public RouterFunction<ServerResponse> routeHello5() {
        return RouterFunctions.route(RequestPredicates.POST("/hello5"), (req) ->
                ServerResponse.ok().body(BodyInserters.fromObject("You said hello to an alternate POST endpoint!")));
    }

    @Bean
    public RouterFunction<ServerResponse> routeHello6() {
        return RouterFunctions.route(RequestPredicates.POST("/hello6"), (req) ->
                ServerResponse.ok().body(BodyInserters.fromObject("You said hello to an alternate POST endpoint!")));
    }

    @Bean
    public RouterFunction<ServerResponse> routeHello7() {
        return RouterFunctions.route(RequestPredicates.POST("/hello7"), (req) ->
                ServerResponse.ok().body(BodyInserters.fromObject("You said hello to an alternate POST endpoint!")));
    }

    @Bean
    public RouterFunction<ServerResponse> routeHello8() {
        return RouterFunctions.route(RequestPredicates.POST("/hello8"), (req) ->
                ServerResponse.ok().body(BodyInserters.fromObject("You said hello to an alternate POST endpoint!")));
    }

    @Bean
    public RouterFunction<ServerResponse> routeHello9() {
        return RouterFunctions.route(RequestPredicates.POST("/hello9"), (req) ->
                ServerResponse.ok().body(BodyInserters.fromObject("You said hello to an alternate POST endpoint!")));
    }

    @Bean
    public RouterFunction<ServerResponse> routeHello10() {
        return RouterFunctions.route(RequestPredicates.POST("/hello10"), (req) ->
                ServerResponse.ok().body(BodyInserters.fromObject("You said hello to an alternate POST endpoint!")));
    }

    @Bean
    public RouterFunction<ServerResponse> routeHello11() {
        return RouterFunctions.route(RequestPredicates.POST("/hello11"), (req) ->
                ServerResponse.ok().body(BodyInserters.fromObject("You said hello to an alternate POST endpoint!")));
    }

    @Bean
    public RouterFunction<ServerResponse> routeHello12() {
        return RouterFunctions.route(RequestPredicates.POST("/hello12"), (req) ->
                ServerResponse.ok().body(BodyInserters.fromObject("You said hello to an alternate POST endpoint!")));
    }

    @Bean
    public RouterFunction<ServerResponse> routeHello13() {
        return RouterFunctions.route(RequestPredicates.POST("/hello13"), (req) ->
                ServerResponse.ok().body(BodyInserters.fromObject("You said hello to an alternate POST endpoint!")));
    }

    @Bean
    public RouterFunction<ServerResponse> routeHello14() {
        return RouterFunctions.route(RequestPredicates.POST("/hello14"), (req) ->
                ServerResponse.ok().body(BodyInserters.fromObject("You said hello to an alternate POST endpoint!")));
    }

    @Bean
    public RouterFunction<ServerResponse> routeHello15() {
        return RouterFunctions.route(RequestPredicates.POST("/hello15"), (req) ->
                ServerResponse.ok().body(BodyInserters.fromObject("You said hello to an alternate POST endpoint!")));
    }

    @Bean
    public RouterFunction<ServerResponse> routeHello16() {
        return RouterFunctions.route(RequestPredicates.POST("/hello16"), (req) ->
                ServerResponse.ok().body(BodyInserters.fromObject("You said hello to an alternate POST endpoint!")));
    }

    @Bean
    public RouterFunction<ServerResponse> routeHello17() {
        return RouterFunctions.route(RequestPredicates.POST("/hello17"), (req) ->
                ServerResponse.ok().body(BodyInserters.fromObject("You said hello to an alternate POST endpoint!")));
    }

    @Bean
    public RouterFunction<ServerResponse> routeHello18() {
        return RouterFunctions.route(RequestPredicates.POST("/hello18"), (req) ->
                ServerResponse.ok().body(BodyInserters.fromObject("You said hello to an alternate POST endpoint!")));
    }
}
