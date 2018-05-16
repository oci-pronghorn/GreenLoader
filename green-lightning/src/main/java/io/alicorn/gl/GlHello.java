package io.alicorn.gl;

import com.ociweb.gl.api.*;
import com.ociweb.json.JSONExtractorImpl;
import com.ociweb.json.JSONExtractorCompleted;
import com.ociweb.json.JSONType;
import com.ociweb.pronghorn.network.HTTPServerConfig;

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

	// Route IDs.
	private int primaryRoute;
	private int alternateRoutes[] = new int[19];
	
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
			conf.logTraffic(false);
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
                new JSONExtractorImpl()
                        .newPath(JSONType.TypeString).completePath("name", "name_a")
                        .newPath(JSONType.TypeBoolean).completePath("happy", "name_b")
                        .newPath(JSONType.TypeInteger).completePath("age", "name_c");

		// Define route for receiving request.
		primaryRoute = builder.defineRoute(extractor).path("/hello").routeId();

		// Extract JSON element IDs.
        name_a = builder.lookupFieldByName(primaryRoute, "name_a");
        name_b = builder.lookupFieldByName(primaryRoute, "name_b");
        name_c = builder.lookupFieldByName(primaryRoute, "name_c");

		// Define topic for publishing from the request receiver to the request responder.
        builder.definePrivateTopic(1 << 16, 100, "/send/200", "consumer", "responder");
        builder.usePrivateTopicsExclusively();
		
		if (telemtry) {
			builder.enableTelemetry();
		}

		// Define other routes.
		for (int i = 0; i < alternateRoutes.length; i++) {
			alternateRoutes[i] = builder.defineRoute().path("/hello" + i).routeId();
		}
	}

	@Override
	public void declareBehavior(GreenRuntime runtime) {	}

	@Override
	public void declareParallelBehavior(GreenRuntime runtime) {

	    // Add a consumer for handling the inbound REST requests.
        runtime.addRestListener("consumer", new RestConsumer(runtime, name_a, name_b, name_c))
				.includeRoutes(primaryRoute);

        // Add a responder for handling the outbound REST responses.
        runtime.addPubSubListener("responder", new RestResponder(runtime))
                .addSubscription("/send/200");

        // Add a consumer/responder for the alternate routes.
		runtime.addRestListener("otherConsumer", new SimpleRestListener(runtime))
				.includeRoutes(alternateRoutes);
	}
}