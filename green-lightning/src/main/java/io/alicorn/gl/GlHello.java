package io.alicorn.gl;

import com.ociweb.gl.api.Builder;
import com.ociweb.gl.api.GreenApp;
import com.ociweb.gl.api.GreenAppParallel;
import com.ociweb.gl.api.GreenCommandChannel;
import com.ociweb.gl.api.GreenRuntime;
import com.ociweb.gl.api.HTTPServerConfig;
import com.ociweb.pronghorn.network.config.HTTPContentTypeDefaults;
import com.ociweb.json.JSONExtractor;
import com.ociweb.json.JSONExtractorCompleted;
import com.ociweb.json.JSONType;

public class GlHello implements GreenAppParallel {
    public static void main(String[] args) {
        GreenRuntime.run(new GlHello(false, Integer.valueOf(args[1]), Boolean.valueOf(args[0]), Boolean.valueOf(args[2])));
    }

    private final int port;
	private final boolean tls;
	private final boolean telemtry;
	private final boolean logging;

	// Trackers for JSON.
	private long name_a, name_b, name_c;
	
	public GlHello(boolean tls, int port, boolean telemtry, boolean logging) {
		this.tls = tls;
		this.port = port;
		this.telemtry = telemtry;
		this.logging = logging;
	}

    @Override
	public void declareConfiguration(Builder builder) {
		HTTPServerConfig conf = builder.useHTTP1xServer(port);

		if (logging) {
			conf.logTraffic();
		}

		conf.setHost("127.0.0.1");
		conf.setConcurrentChannelsPerDecryptUnit(10);
		conf.setConcurrentChannelsPerEncryptUnit(10);
		builder.parallelTracks(4);
		
		if (!tls) {
			conf.useInsecureServer();
		}

		// Define JSON extraction behavior.
        JSONExtractorCompleted extractor =
                new JSONExtractor()
                        .newPath(JSONType.TypeString).key("name").completePath("name_a")
                        .newPath(JSONType.TypeBoolean).key("happy").completePath("name_b")
                        .newPath(JSONType.TypeInteger).key("age").completePath("name_c");

		// Define route for receiving request.
		int routeId = builder.defineRoute(extractor).path("/hello").routeId();

		// Extract JSON element IDs.
        name_a = builder.lookupFieldByName(routeId, "name_a");
        name_b = builder.lookupFieldByName(routeId, "name_b");
        name_c = builder.lookupFieldByName(routeId, "name_c");

		// Define topic for publishing from the request receiver to the request responder.
        builder.definePrivateTopic(1 << 16, 100, "/send/200", "consumer", "responder");
        builder.usePrivateTopicsExclusively();
		
		if (telemtry) {
			builder.enableTelemetry();
		}
	}

	@Override
	public void declareBehavior(GreenRuntime runtime) {	}

	@Override
	public void declareParallelBehavior(GreenRuntime runtime) {

	    // Add a consumer for handling the inbound REST requests.
        runtime.addRestListener("consumer", new RestConsumer(runtime, name_a, name_b, name_c))
                .includeAllRoutes();

        // Add a responder for handling all outbound REST responses.
        runtime.addPubSubListener("responder", new RestResponder(runtime))
                .addSubscription("/send/200");
	}
}