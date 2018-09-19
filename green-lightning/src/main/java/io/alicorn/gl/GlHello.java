package io.alicorn.gl;

import com.ociweb.gl.api.GreenApp;
import com.ociweb.gl.api.GreenFramework;
import com.ociweb.gl.api.GreenRuntime;
import com.ociweb.pronghorn.network.HTTPServerConfig;
import com.ociweb.pronghorn.struct.StructType;

public class GlHello implements GreenApp {
    public static void main(String[] args) {
        GreenRuntime.run(new GlHello(false, Integer.valueOf(args[1]), Boolean.valueOf(args[0]), Boolean.valueOf(args[2])));
    }

    private final int port;
	private final boolean tls;
	private final boolean telemtry;
	private final boolean logging;


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
	public void declareConfiguration(GreenFramework builder) {
    	
    	
		HTTPServerConfig conf = builder.useHTTP1xServer(port, 2, this::declareParallelBehavior);

		if (logging) {
			conf.logTraffic(false);
		}

		conf.setHost("127.0.0.1");
		conf.setConcurrentChannelsPerDecryptUnit(2);
		conf.setConcurrentChannelsPerEncryptUnit(2);
		conf.setMaxRequestSize(1<<14);
		
		
		if (!tls) {
			conf.useInsecureServer();
		}

		// Define route for receiving request.
		primaryRoute = builder.defineRoute()
				.parseJSON()					
					.stringField("name", Field.NAME)
					.booleanField("happy", Field.HAPPY)
					.integerField("age", Field.AGE)
				.path("/hello").routeId();


		// Define topic for publishing from the request receiver to the request responder.
        builder.definePrivateTopic(1 << 14, 100, "/send/200", "consumer", "responder");
       
        builder.usePrivateTopicsExclusively();
		
        builder.defineStruct()
               .addField("name", StructType.Text, Field.NAME)
               .addField("happy", StructType.Boolean, Field.HAPPY)
               .addField("age", StructType.Integer, Field.AGE)
               .register(Struct.PAYLOAD);
               
               
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

	
	public void declareParallelBehavior(GreenRuntime runtime) {

	    // Add a consumer for handling the inbound REST requests.
        runtime.addRestListener("consumer", new RestConsumer(runtime))
				.includeRoutes(primaryRoute);

        // Add a responder for handling the outbound REST responses.
        runtime.addPubSubListener("responder", new RestResponder(runtime))
                .addSubscription("/send/200");

        // Add a consumer/responder for the alternate routes.
		runtime.addRestListener("otherConsumer", new SimpleRestListener(runtime))
				.includeRoutes(alternateRoutes);
	}
}