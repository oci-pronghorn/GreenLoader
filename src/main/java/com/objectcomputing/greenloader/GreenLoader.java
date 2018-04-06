package com.objectcomputing.greenloader;

import com.ociweb.gl.api.GreenRuntime;
import com.ociweb.gl.test.ParallelClientLoadTester;
import com.ociweb.gl.test.ParallelClientLoadTesterConfig;
import com.ociweb.gl.test.ParallelClientLoadTesterPayload;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Automated load tester for various microservice frameworks using the Green
 * Lightning parallel client load tester.
 *
 * @author Brandon Sanders [brandon@alicorn.io]
 */
public class GreenLoader {

    private static final Logger logger = LoggerFactory.getLogger(GreenLoader.class);

    // 10_000 = 8 minutes.
    public static final int CYCLES_PER_TRACK = 10_000;
    public static final long CYCLE_RATE = 1_200L;
    public static final long DURATION = 60_000L;

    public static void main(String[] args) throws Exception {

        // Parse the load configuration.
        JsonObject file = Json.parse(new InputStreamReader(new FileInputStream(new File("load-config.json")))).asObject();
        JsonArray config = file.get("services").asArray();

        // Iterate over all services.
        for (JsonValue serviceValue : config) {

            // Get the service as an object.
            JsonObject serviceObject = serviceValue.asObject();

            // Begin load test.
            logger.info("===============================================================================");
            logger.info("Beginning load test for: {}", serviceObject.getString("name", null));

            // Start the service.
            Process process = Runtime.getRuntime().exec(serviceObject.getString("start", null));

            // Configure payload.
            ParallelClientLoadTesterPayload loadTesterPayload
                    = new ParallelClientLoadTesterPayload(serviceObject.get("payload").toString());

            // Configure load tester.
            ParallelClientLoadTesterConfig loadTesterConfig =
                    new ParallelClientLoadTesterConfig(1,
                                                       CYCLES_PER_TRACK,
                                                       serviceObject.getInt("port", -1),
                                                       serviceObject.getString("endpoint", null),
                                                       false);
            loadTesterConfig.simultaneousRequestsPerTrackBits  = 0;
            loadTesterConfig.responseTimeoutNS = 0L;
            loadTesterConfig.cycleRate = CYCLE_RATE;

            // Wait for service start.
            logger.info("Starting service via {}", serviceObject.getString("start", null));
            Thread.sleep(10_000);
            logger.info("Started service via {}", serviceObject.getString("start", null));

            // Run load tester.
            logger.info("Starting load test.");
            GreenRuntime.testConcurrentUntilShutdownRequested(
                    new ParallelClientLoadTester(loadTesterConfig, loadTesterPayload),
                    DURATION);
            logger.info("Load test complete.");

            // Stop service.
            logger.info("Stopping service.");
            process.destroy();
        }

        // Quit.
        System.exit(0);;
    }
}