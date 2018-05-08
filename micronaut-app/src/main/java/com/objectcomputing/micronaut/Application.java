package com.objectcomputing.micronaut;

import io.micronaut.runtime.Micronaut;

import java.util.logging.Level;

public class Application {
    public static void main(String[] args) {

        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(ch.qos.logback.classic.Level.INFO);

        Micronaut.run(Application.class);
    }
}