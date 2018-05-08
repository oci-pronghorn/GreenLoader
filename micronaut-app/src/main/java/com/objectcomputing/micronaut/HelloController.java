package com.objectcomputing.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.reactivex.Single;

import javax.inject.Singleton;

@Controller("/")
@Singleton
public class HelloController {

    @Post("/hello")
    public Single<HttpResponse<JsonResponse>> hello(@Body Single<JsonRequest> jsonRequest) {
        return jsonRequest.map(req ->
            HttpResponse.created(new JsonResponse("Hello, " + req.getName() + "!", !req.isHappy(), req.getAge() * 2))
        );
    }

    // Response to POST requests on other endpoints, too!
    // TODO: Dynamically register 19 other endpoints instead of hideously manually.
    @Post("/hello0")
    public Single<HttpResponse<String>> hello0(@Body Single<JsonRequest> jsonRequest) {
        return Single.just(HttpResponse.ok("You said hello to an alternate POST endpoint!"));
    }

    @Post("/hello1")
    public Single<HttpResponse<String>> hello1(@Body Single<JsonRequest> jsonRequest) {
        return Single.just(HttpResponse.ok("You said hello to an alternate POST endpoint!"));
    }

    @Post("/hello2")
    public Single<HttpResponse<String>> hello2(@Body Single<JsonRequest> jsonRequest) {
        return Single.just(HttpResponse.ok("You said hello to an alternate POST endpoint!"));
    }

    @Post("/hello3")
    public Single<HttpResponse<String>> hello3(@Body Single<JsonRequest> jsonRequest) {
        return Single.just(HttpResponse.ok("You said hello to an alternate POST endpoint!"));
    }

    @Post("/hello4")
    public Single<HttpResponse<String>> hello4(@Body Single<JsonRequest> jsonRequest) {
        return Single.just(HttpResponse.ok("You said hello to an alternate POST endpoint!"));
    }

    @Post("/hello5")
    public Single<HttpResponse<String>> hello5(@Body Single<JsonRequest> jsonRequest) {
        return Single.just(HttpResponse.ok("You said hello to an alternate POST endpoint!"));
    }

    @Post("/hello6")
    public Single<HttpResponse<String>> hello6(@Body Single<JsonRequest> jsonRequest) {
        return Single.just(HttpResponse.ok("You said hello to an alternate POST endpoint!"));
    }

    @Post("/hello7")
    public Single<HttpResponse<String>> hello7(@Body Single<JsonRequest> jsonRequest) {
        return Single.just(HttpResponse.ok("You said hello to an alternate POST endpoint!"));
    }

    @Post("/hello8")
    public Single<HttpResponse<String>> hello8(@Body Single<JsonRequest> jsonRequest) {
        return Single.just(HttpResponse.ok("You said hello to an alternate POST endpoint!"));
    }

    @Post("/hello9")
    public Single<HttpResponse<String>> hello9(@Body Single<JsonRequest> jsonRequest) {
        return Single.just(HttpResponse.ok("You said hello to an alternate POST endpoint!"));
    }

    @Post("/hello10")
    public Single<HttpResponse<String>> hello10(@Body Single<JsonRequest> jsonRequest) {
        return Single.just(HttpResponse.ok("You said hello to an alternate POST endpoint!"));
    }

    @Post("/hello11")
    public Single<HttpResponse<String>> hello11(@Body Single<JsonRequest> jsonRequest) {
        return Single.just(HttpResponse.ok("You said hello to an alternate POST endpoint!"));
    }

    @Post("/hello12")
    public Single<HttpResponse<String>> hello12(@Body Single<JsonRequest> jsonRequest) {
        return Single.just(HttpResponse.ok("You said hello to an alternate POST endpoint!"));
    }

    @Post("/hello13")
    public Single<HttpResponse<String>> hello13(@Body Single<JsonRequest> jsonRequest) {
        return Single.just(HttpResponse.ok("You said hello to an alternate POST endpoint!"));
    }

    @Post("/hello14")
    public Single<HttpResponse<String>> hello14(@Body Single<JsonRequest> jsonRequest) {
        return Single.just(HttpResponse.ok("You said hello to an alternate POST endpoint!"));
    }

    @Post("/hello15")
    public Single<HttpResponse<String>> hello15(@Body Single<JsonRequest> jsonRequest) {
        return Single.just(HttpResponse.ok("You said hello to an alternate POST endpoint!"));
    }

    @Post("/hello16")
    public Single<HttpResponse<String>> hello16(@Body Single<JsonRequest> jsonRequest) {
        return Single.just(HttpResponse.ok("You said hello to an alternate POST endpoint!"));
    }

    @Post("/hello17")
    public Single<HttpResponse<String>> hello17(@Body Single<JsonRequest> jsonRequest) {
        return Single.just(HttpResponse.ok("You said hello to an alternate POST endpoint!"));
    }

    @Post("/hello18")
    public Single<HttpResponse<String>> hello18(@Body Single<JsonRequest> jsonRequest) {
        return Single.just(HttpResponse.ok("You said hello to an alternate POST endpoint!"));
    }
}