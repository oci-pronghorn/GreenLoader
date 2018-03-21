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
        String name = Json.parse(httpEntity.getBody()).asObject().get("name").asString();
        JsonObject response = new JsonObject();
        response.set("data", "Hello, " + name + "!");
        return response.toString();
    }
}