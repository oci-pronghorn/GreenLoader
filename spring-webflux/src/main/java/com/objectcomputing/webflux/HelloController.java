package com.objectcomputing.webflux;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author Brandon Sanders [brandon@alicorn.io]
 */
@Component
public class HelloController {

    public Mono<ServerResponse> hello(ServerRequest request) {
        return request
            .bodyToMono(JsonRequest.class)
            .flatMap(req ->
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromObject(
                                new JsonResponse("Hello, " + req.getName() + "!", !req.isHappy(), req.getAge() * 2))));
    }
}
