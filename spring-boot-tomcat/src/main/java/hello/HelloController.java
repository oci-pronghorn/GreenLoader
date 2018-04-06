package hello;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

import com.eclipsesource.json.*;

@RestController
public class HelloController {

    @RequestMapping(value="/hello", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String index(HttpEntity<String> httpEntity) {
        JsonObject req = Json.parse(httpEntity.getBody()).asObject();
        JsonObject response = new JsonObject();
        response.set("message", "Hello, " + req.getString("name", null) + "!");
        response.set("happy", !req.getBoolean("happy", false));
        response.set("age", req.getInt("age", 0) * 2);
        return response.toString();
    }
}