package hello;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

@RestController
public class HelloController {

    @RequestMapping(value="/hello", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public JsonResponse index(@RequestBody JsonRequest req) {
        return new JsonResponse("Hello, " + req.getName() + "!", !req.isHappy(), req.getAge() * 2);
    }

    // Response to POST requests on other endpoints, too!
    // TODO: Dynamically register 19 other endpoints instead of hideously manually.
    @RequestMapping(value="/hello0", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public String hello0(@RequestBody JsonRequest req) {
        return "You said hello to an alternate POST endpoint!";
    }

    @RequestMapping(value="/hello1", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public String hello1(@RequestBody JsonRequest req) {
        return "You said hello to an alternate POST endpoint!";
    }

    @RequestMapping(value="/hello2", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public String hello2(@RequestBody JsonRequest req) {
        return "You said hello to an alternate POST endpoint!";
    }

    @RequestMapping(value="/hello3", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public String hello3(@RequestBody JsonRequest req) {
        return "You said hello to an alternate POST endpoint!";
    }

    @RequestMapping(value="/hello4", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public String hello4(@RequestBody JsonRequest req) {
        return "You said hello to an alternate POST endpoint!";
    }

    @RequestMapping(value="/hello5", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public String hello5(@RequestBody JsonRequest req) {
        return "You said hello to an alternate POST endpoint!";
    }

    @RequestMapping(value="/hello6", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public String hello6(@RequestBody JsonRequest req) {
        return "You said hello to an alternate POST endpoint!";
    }

    @RequestMapping(value="/hello7", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public String hello7(@RequestBody JsonRequest req) {
        return "You said hello to an alternate POST endpoint!";
    }

    @RequestMapping(value="/hello8", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public String hello8(@RequestBody JsonRequest req) {
        return "You said hello to an alternate POST endpoint!";
    }

    @RequestMapping(value="/hello9", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public String hello9(@RequestBody JsonRequest req) {
        return "You said hello to an alternate POST endpoint!";
    }

    @RequestMapping(value="/hello10", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public String hello10(@RequestBody JsonRequest req) {
        return "You said hello to an alternate POST endpoint!";
    }

    @RequestMapping(value="/hello11", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public String hello11(@RequestBody JsonRequest req) {
        return "You said hello to an alternate POST endpoint!";
    }

    @RequestMapping(value="/hello12", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public String hello12(@RequestBody JsonRequest req) {
        return "You said hello to an alternate POST endpoint!";
    }

    @RequestMapping(value="/hello13", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public String hello13(@RequestBody JsonRequest req) {
        return "You said hello to an alternate POST endpoint!";
    }

    @RequestMapping(value="/hello14", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public String hello14(@RequestBody JsonRequest req) {
        return "You said hello to an alternate POST endpoint!";
    }

    @RequestMapping(value="/hello15", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public String hello15(@RequestBody JsonRequest req) {
        return "You said hello to an alternate POST endpoint!";
    }

    @RequestMapping(value="/hello16", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public String hello16(@RequestBody JsonRequest req) {
        return "You said hello to an alternate POST endpoint!";
    }

    @RequestMapping(value="/hello17", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public String hello17(@RequestBody JsonRequest req) {
        return "You said hello to an alternate POST endpoint!";
    }

    @RequestMapping(value="/hello18", method=RequestMethod.POST, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public String hello18(@RequestBody JsonRequest req) {
        return "You said hello to an alternate POST endpoint!";
    }
}