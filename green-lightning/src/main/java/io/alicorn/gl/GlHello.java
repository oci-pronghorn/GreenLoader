package io.alicorn.gl;

import com.ociweb.gl.api.Builder;
import com.ociweb.gl.api.GreenApp;
import com.ociweb.gl.api.GreenAppParallel;
import com.ociweb.gl.api.GreenCommandChannel;
import com.ociweb.gl.api.GreenRuntime;
import com.ociweb.gl.api.HTTPServerConfig;
import com.ociweb.pronghorn.network.config.HTTPContentTypeDefaults;

public class GlHello implements GreenAppParallel {
    public static void main(String[] args) {
        GreenRuntime.run(new GlHello(false, 3305, false));
    }

    private final int port;
	private final boolean tls;
	private final boolean telemtry;
	
	public GlHello(boolean tls, int port, boolean telemtry) {
		this.tls = tls;
		this.port = port;
		this.telemtry = telemtry;
	}

    @Override
	public void declareConfiguration(Builder builder) {
		HTTPServerConfig conf = builder.useHTTP1xServer(port).setHost("127.0.0.1");

		conf.setConcurrentChannelsPerDecryptUnit(10);
		builder.parallelism(2);
		conf.setConcurrentChannelsPerEncryptUnit(10);
		
		if (!tls) {
			conf.useInsecureServer();
		}
				
		builder.defineRoute("/hello");
		
		if (telemtry) {
			builder.enableTelemetry();
		}
	}

	@Override
	public void declareBehavior(GreenRuntime runtime) {	}

	@Override
	public void declareParallelBehavior(GreenRuntime runtime) {
		final GreenCommandChannel cmd = runtime.newCommandChannel(NET_RESPONDER);
		
		runtime.addRestListener((r)->{
			return cmd.publishHTTPResponse(r, 200, HTTPContentTypeDefaults.TXT, (w)->{
				w.append("Hello, World!");
			});
		}).includeAllRoutes();	
	}
}