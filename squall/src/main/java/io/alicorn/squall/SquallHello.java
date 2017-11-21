package io.alicorn.squall;

import java.lang.Exception;
import java.lang.Thread;

import io.alicorn.squall.api.*;
import io.alicorn.squall.api.http.*;

public class SquallHello {
    public static void main(String[] args) {
        Squall squall = new Squall();
        squall.setPort(3306);
        squall.onGet("/hello", new HttpRequestHandler() {
            public void handle(HttpRequestReader reader, HttpResponseWriter writer) throws Exception {
                writer.send("Hello, world!");
            }
        });
        squall.start();
        while (true) { 
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}