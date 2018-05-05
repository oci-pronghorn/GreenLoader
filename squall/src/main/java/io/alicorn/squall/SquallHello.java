package io.alicorn.squall;

import java.lang.Exception;
import java.lang.Thread;

import com.eclipsesource.json.*;

import io.alicorn.squall.api.*;
import io.alicorn.squall.api.http.*;

public class SquallHello {
    public static void main(String[] args) {
        Squall squall = Squall.create(3307);
        squall.onPost("/hello", (reader, writer) -> {
            JsonObject req = Json.parse(reader.getBody()).asObject();
            JsonObject response = new JsonObject();
            response.set("message", "Hello, " + req.getString("name", null) + "!");
            response.set("happy", !req.getBoolean("happy", false));
            response.set("age", req.getInt("age", 0) * 2);
            writer.setContentType(HttpContentType.APPLICATION_JSON);
            writer.send(response.toString());
        });

        for (int i = 0; i < 19; i++) {
            squall.onPost("/hello" + i, (req, res) -> {
                res.send("You said hello to an alternate POST endpoint!");
            });
        }

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