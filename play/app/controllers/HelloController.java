package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.*;

public class HelloController extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    public Result hello() {

        // Get request JSON.
        JsonNode json = request().body().asJson();

        // Prepare response JSON.
        ObjectNode response = Json.newObject();

        // Build response JSON.
        response.put("message", "Hello, " + json.findPath("name").textValue() + "!");
        response.put("age", json.findPath("age").intValue() * 2);
        response.put("happy", !json.findPath("happy").booleanValue());

        // Return response JSON.
        return ok(response);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hello0() {
        return ok("You said hello to an alternate POST endpoint!");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hello1() {
        return ok("You said hello to an alternate POST endpoint!");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hello2() {
        return ok("You said hello to an alternate POST endpoint!");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hello3() {
        return ok("You said hello to an alternate POST endpoint!");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hello4() {
        return ok("You said hello to an alternate POST endpoint!");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hello5() {
        return ok("You said hello to an alternate POST endpoint!");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hello6() {
        return ok("You said hello to an alternate POST endpoint!");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hello7() {
        return ok("You said hello to an alternate POST endpoint!");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hello8() {
        return ok("You said hello to an alternate POST endpoint!");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hello9() {
        return ok("You said hello to an alternate POST endpoint!");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hello10() {
        return ok("You said hello to an alternate POST endpoint!");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hello11() {
        return ok("You said hello to an alternate POST endpoint!");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hello12() {
        return ok("You said hello to an alternate POST endpoint!");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hello13() {
        return ok("You said hello to an alternate POST endpoint!");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hello14() {
        return ok("You said hello to an alternate POST endpoint!");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hello15() {
        return ok("You said hello to an alternate POST endpoint!");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hello16() {
        return ok("You said hello to an alternate POST endpoint!");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hello17() {
        return ok("You said hello to an alternate POST endpoint!");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result hello18() {
        return ok("You said hello to an alternate POST endpoint!");
    }
}
