package com.objectcomputing.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.http.server.netty.NettyHttpServer;

public class Micronaut {

    public static void main(String[] args) {
//        io.micronaut.runtime.Micronaut.run(Micronaut.class, args);

        EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer.class);

        System.out.println("Hi...");
    }
}