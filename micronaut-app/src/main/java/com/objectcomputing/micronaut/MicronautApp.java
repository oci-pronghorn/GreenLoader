package com.objectcomputing.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import javax.inject.Singleton;

@Controller("/hello")
@Singleton
public class MicronautApp {
    @Get("/")
    public String index() {
        return "Hello World";
    }
}