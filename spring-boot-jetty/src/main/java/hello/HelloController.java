package hello;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

@RestController
public class HelloController {

    @RequestMapping(value="/hello", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public JsonResponse index(@RequestBody JsonRequest req) {
        return new JsonResponse("Hello, " + req.getName() + "!", !req.isHappy(), req.getAge() * 2);
    }
}