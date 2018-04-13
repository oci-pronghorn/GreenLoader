package hello;

import java.nio.charset.StandardCharsets;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.eclipsesource.json.*;

@RestController
public class HelloController {

    @RequestMapping(value="/hello", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> index(HttpEntity<String> httpEntity) {
        JsonObject req = Json.parse(httpEntity.getBody()).asObject();
        JsonObject response = new JsonObject();
        response.set("message", "Hello, " + req.getString("name", null) + "!");
        response.set("happy", !req.getBoolean("happy", false));
        response.set("age", req.getInt("age", 0) * 2);

        String responseString = response.toString();
        
        return ResponseEntity.ok().contentLength(responseString.getBytes(StandardCharsets.UTF_8).length).body(
                responseString);
    }
}