package io.alicorn.squall;

import java.lang.Exception;
import java.lang.Thread;

import com.eclipsesource.json.*;

import io.alicorn.squall.api.*;
import io.alicorn.squall.api.http.*;

public class SquallHello {
    public static void main(String[] args) {
        Squall squall = Squall.create(3307);
        squall.onPost("/hello", new HttpRequestHandler() {
            public void handle(HttpRequestReader reader, HttpResponseWriter writer) throws Exception {
                JsonObject req = Json.parse(reader.getBody()).asObject();
                JsonObject response = new JsonObject();
                response.set("message", "Hello, " + req.getString("name", null) + "!");
                response.set("happy", !req.getBoolean("happy", false));
                response.set("age", req.getInt("age", 0) * 2);
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