package com.ociweb.ph;

import java.util.concurrent.TimeUnit;

import com.ociweb.gl.api.GreenRuntime;
import com.ociweb.gl.test.ParallelClientLoadTester;
import com.ociweb.gl.test.ParallelClientLoadTesterConfig;
import com.ociweb.gl.test.ParallelClientLoadTesterPayload;

public class LoadTest {

	
	public static void main(String[] args) {
		
		boolean tls = false;
		boolean telemetry = false;
		boolean logging = false;
		PHWebServer server = new PHWebServer(tls, 8088, telemetry, logging);


		ParallelClientLoadTesterPayload payload
				= new ParallelClientLoadTesterPayload("{\"name\":\"nathan\",\"happy\":true,\"age\":42}");

		int cyclesPerTrack = 300_000; //*(1+99_9999);// / 10;
		int parallelTracks = 1;

		ParallelClientLoadTesterConfig config2 =
				new ParallelClientLoadTesterConfig(parallelTracks, cyclesPerTrack, 8088, "/hello", telemetry);

		//TODO: the pipes between private topics may not be large enough for this...
		config2.simultaneousRequestsPerTrackBits  = 0; // 126k for max volume
		config2.warmup = 3000;

		GreenRuntime.testConcurrentUntilShutdownRequested(
				new ParallelClientLoadTester(config2, payload),
				5*60*60_000); //5 hours

		server.defaultScheduler.shutdown();
		server.defaultScheduler.awaitTermination(1, TimeUnit.SECONDS);
		
				
	}
	
}
