package io.alicorn.squall;

import java.lang.Exception;
import java.lang.Thread;

import com.eclipsesource.json.*;

import io.alicorn.squall.api.*;
import io.alicorn.squall.api.http.*;

public class SquallHello {
    public static void main(String[] args) {
        Squall squall = Squall.create(3306);
        squall.onPost("/hello", new HttpRequestHandler() {
            public void handle(HttpRequestReader reader, HttpResponseWriter writer) throws Exception {
                String name = Json.parse(reader.getBody()).asObject().get("name").asString();
                JsonObject response = new JsonObject();
                response.set("data", "Hello, " + name + "!");
                writer.setContentType(HttpContentType.APPLICATION_JSON);
                writer.send(response.toString());
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