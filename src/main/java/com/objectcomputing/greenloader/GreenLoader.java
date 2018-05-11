package com.objectcomputing.greenloader;

import com.ociweb.gl.api.Builder;
import com.ociweb.gl.api.GreenRuntime;
import com.ociweb.gl.test.ParallelClientLoadTester;
import com.ociweb.gl.test.ParallelClientLoadTesterConfig;
import com.ociweb.gl.test.ParallelClientLoadTesterPayload;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.ociweb.pronghorn.network.ClientSocketWriterStage;
import com.ociweb.pronghorn.network.ServerSocketWriterStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.StringTokenizer;

/**
 * Automated load tester for various microservice frameworks using the Green
 * Lightning parallel client load tester.
 *
 * @author Brandon Sanders [brandon@alicorn.io]
 */
public class GreenLoader {

    private static final Logger logger = LoggerFactory.getLogger(GreenLoader.class);

    // 10_000 = 8 minutes.
    public static final int CYCLES_PER_TRACK = 2_000_000; // 2_000_000
    public static final long CYCLE_RATE = 4_000L;
    public static final long DURATION = 3 * 60 * 60_000L; // 3*60*60_000L

    public static void main(String[] args) throws Exception {

        // File name.
        String fileName = "load-config.json";
        int bits = 0;
        if (args.length > 0) {
            fileName = args[0];
            bits = Integer.parseInt(args[1]);
        }

        // If there's a third arg, run in "quick" mode.
        // This logs traffic and allows users to configure a short cycle rate for debugging.
        int cycles = CYCLES_PER_TRACK;
        boolean logTraffic = false;
        if (args.length == 3) {
            cycles = Integer.parseInt(args[2]);
            logTraffic = true;
        }

        // Enable or disable traffic logging.
        ClientSocketWriterStage.showWrites = logTraffic;

        // Parse the load configuration.
        JsonObject file = Json.parse(new InputStreamReader(new FileInputStream(new File(fileName)))).asObject();
        JsonArray config = file.get("services").asArray();

        // Print working directory.
        logger.info("Starting load tests in working directory: {}", System.getProperty("user.dir"));

        // Iterate over all services.
        for (JsonValue serviceValue : config) {

            // Get the service as an object.
            JsonObject serviceObject = serviceValue.asObject();

            // Get the service name. We append the number of bits in order to differentiate runs.
            String fqnServiceName = serviceObject.getString("name", null) + "-" + String.valueOf(bits);

            // Begin load test.
            logger.info("===============================================================================");
            logger.info("Beginning load test for: {}", fqnServiceName);

            // Start the service.
            logger.info("Starting service via {}", serviceObject.getString("start", null));
            StringTokenizer st = new StringTokenizer(serviceObject.getString("start", null));
            String[] cmdarray = new String[st.countTokens()];
            for (int i = 0; st.hasMoreTokens(); i++) {
                cmdarray[i] = st.nextToken();
            }
            ProcessBuilder builder = new ProcessBuilder(cmdarray);
            File outfile = Paths.get("greenloader-" + fqnServiceName + "-log.txt").toFile();
            outfile.delete();
            outfile.createNewFile();
            builder.redirectErrorStream(true);
            builder.redirectOutput(outfile);
            Process process = builder.start();
            long startTime = System.currentTimeMillis();

            // Configure payload.
            ParallelClientLoadTesterPayload loadTesterPayload
                    = new ParallelClientLoadTesterPayload(serviceObject.get("payload").toString());

            // Configure load tester.
            ParallelClientLoadTesterConfig loadTesterConfig =
                    new ParallelClientLoadTesterConfig(1,
                                                       cycles,
                                                       serviceObject.getInt("port", -1),
                                                       serviceObject.getString("endpoint", null),
                                                       false);
            loadTesterConfig.insecureClient = true;
            loadTesterConfig.host = "127.0.0.1";
            loadTesterConfig.simultaneousRequestsPerTrackBits = bits;
            loadTesterConfig.cycleRate = CYCLE_RATE;

            // Wait for service start.
            logger.info("Waiting for service port to become available...");
            while (true) {
                Socket s = null;
                try {
                    s = new Socket(loadTesterConfig.host, loadTesterConfig.port);
                    break;
                } catch (Exception e) {
                    // No port yet.
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e2) {
                        e.printStackTrace();
                    }
                } finally {
                    if (s != null) {
                        try {
                            s.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            logger.info("Service port became available {}ms (+/- 1ms) after startup.",
                        System.currentTimeMillis() - startTime);

            // Run load tester.
            logger.info("Starting load test against {}{} on port {} with {} parallel bits.", 
                        loadTesterConfig.host, loadTesterConfig.route, loadTesterConfig.port, bits);
            ParallelClientLoadTester tester = new ParallelClientLoadTester(loadTesterConfig, loadTesterPayload);
            GreenRuntime.testConcurrentUntilShutdownRequested(tester, DURATION);
            logger.info("Load test complete.");

            // Stop service.
            logger.info("Stopping service.");
            process.destroy();
        }

        // Quit.
        System.exit(0);;
    }
}