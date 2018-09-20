package com.objectcomputing.greenloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.ociweb.gl.api.ArgumentParser;
import com.ociweb.gl.api.GreenRuntime;
import com.ociweb.gl.test.ParallelClientLoadTester;
import com.ociweb.gl.test.ParallelClientLoadTesterConfig;
import com.ociweb.gl.test.ParallelClientLoadTesterPayload;
import com.ociweb.pronghorn.network.ClientAbandonConnectionScanner;
import com.ociweb.pronghorn.network.ClientSocketReaderStage;
import com.ociweb.pronghorn.network.ClientSocketWriterStage;

/**
 * Automated load tester for various microservice frameworks using the Green
 * Lightning parallel client load tester.
 *
 * @author Brandon Sanders [brandon@alicorn.io]
 */
public class GreenLoader {

    private static final Logger logger = LoggerFactory.getLogger(GreenLoader.class);

    public static final int CYCLES_PER_TRACK = 3_000_000;
    public static final long CYCLE_RATE = 4_000L;

    // 40 minute maximum test duration.
    public static final long DURATION = (1_000 * 60) * 40;

    public static void main(String[] args) throws Exception {

        // Parse arguments.
        ArgumentParser arguments = new ArgumentParser(args);
        final String fileName = arguments.getArgumentValue("--file", "-f", "load-config.json");
        final int bits = arguments.getArgumentValue("--bits", "-b", 0);
        final int tracks = arguments.getArgumentValue("--tracks", "-t", 1);
        final int cycles = arguments.getArgumentValue("--cycles", "-c", CYCLES_PER_TRACK);
        final boolean logTraffic = arguments.getArgumentValue("--log", "-l", false);

        // Enable or disable traffic logging.
        ClientSocketWriterStage.showWrites = logTraffic;

        ClientSocketReaderStage.abandonSlowConnections = false;

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
            String fqnServiceName = serviceObject.getString("name", null) +
                    "-b" + String.valueOf(bits) +
                    "-t" + String.valueOf(tracks);

            // Begin load test.
            logger.info("===============================================================================");
            logger.info("              BEGINNING LOAD TEST FOR: {}", fqnServiceName);
            logger.info("===============================================================================");
            
            // Start the service.
            StringTokenizer st = new StringTokenizer(serviceObject.getString("start", null));
            String[] cmdarray = new String[st.countTokens()];
            for (int i = 0; st.hasMoreTokens(); i++) {
                cmdarray[i] = st.nextToken();
            }
            logger.info("Starting service via command array {}", Arrays.toString(cmdarray));
            ProcessBuilder builder = new ProcessBuilder(cmdarray);
            File outfile = Paths.get("logs", "greenloader-" + fqnServiceName + "-log.txt").toFile();
            outfile.getParentFile().mkdirs();
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
                    new ParallelClientLoadTesterConfig(tracks,
                                                       cycles,
                                                       serviceObject.getInt("port", -1),
                                                       serviceObject.getString("endpoint", null),
                                                       false);
            loadTesterConfig.insecureClient = true;
            loadTesterConfig.host = "127.0.0.1";
            loadTesterConfig.simultaneousRequestsPerTrackBits = bits;
            loadTesterConfig.cycleRate = CYCLE_RATE;
            loadTesterConfig.parallelTracks = 1;

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
            logger.info("Load test complete for {}.",fqnServiceName);
            
            // Stop service.
            logger.info("Stopping service.");
            process.destroy();
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Quit.
        System.exit(0);;
    }
}