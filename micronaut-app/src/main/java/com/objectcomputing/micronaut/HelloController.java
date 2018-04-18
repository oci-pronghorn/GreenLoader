package com.objectcomputing.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import io.reactivex.Single;

import javax.inject.Singleton;

@Controller("/")
@Singleton
@Validated
public class HelloController {

    @Post("/hello")
    public Single<HttpResponse<JsonResponse>> hello(@Body Single<JsonRequest> jsonRequest) {
        return jsonRequest.map(req ->
            HttpResponse.created(new JsonResponse("Hello, " + req.getName() + "!", !req.isHappy(), req.getAge() * 2))
        );
    }
}