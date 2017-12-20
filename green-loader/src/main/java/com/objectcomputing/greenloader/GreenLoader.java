package com.objectcomputing.greenloader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.concurrent.*;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.locks.LockSupport;

import com.ning.http.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GreenLoader {

    private static final Logger logger = LoggerFactory.getLogger(GreenLoader.class);

    /**
     * Kicks off a load test on a service.
     *
     * @param host
     * @param method
     * @param payload
     * @param workers
     * @param requestsPerWorkerPerSecond
     * @param seconds
     *
     * @return A two-dimensional {@code long} array where the first dimension is the index of a result and the second dimension contains two values:
     * - [0]: the response time in nanoseconds of the result's response.
     * - [1]: the HTTP status code of the result's response.
     * - [2]: the UNIX timestamp of the response.
     */
    private static long[][] applyLoad(final String host, final String method, final String payload, final int workers, final int requestsPerWorkerPerSecond, final int seconds) {

        // Results array.
        final long[][] results = new long[workers * requestsPerWorkerPerSecond * seconds][3];

        // Worker threads manager.
        ForkJoinPool fjp = new ForkJoinPool(Math.min(workers, Runtime.getRuntime().availableProcessors() / 2));

        // Submit workers.
        for (int i = 0; i < workers; i++) {

            // Calculate array offset idx.
            final int resultsOffset = i * requestsPerWorkerPerSecond * seconds;

            // Submit worker.
            fjp.execute(new Runnable() {
                @Override
                public void run() {
                    // Start and stop time for timing.
                    long start, difference = 0;

                    // Calculate nanoseconds between requests per second.
                    final long nanosBetweenRequests = (long) (1.0E-9 / requestsPerWorkerPerSecond);

                    // Prepare Apache request.
                    AsyncHttpClientConfig.Builder config = new AsyncHttpClientConfig.Builder();
                    config.setAllowPoolingConnections(true);
                    config.setAcceptAnyCertificate(true);
                    config.setFollowRedirect(true);
                    config.setUserAgent("GreenLoader");
                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient(config.build());
                    Request request;

                    // GET or POST?
                    if ("GET".equalsIgnoreCase(method)) {
                        request = asyncHttpClient.prepareGet(host).build();
                    } else if ("POST".equalsIgnoreCase(method)) {
                        request = asyncHttpClient.preparePost(host).setBody(payload).build();
                    } else {
                        throw new IllegalArgumentException("GreenLoader only supports POST and GET at this time.");
                    }

                    // Perform all requests.
                    for (int i = 0; i < requestsPerWorkerPerSecond * seconds; i++) {
                        try {

                            // Log start time.
                            start = System.nanoTime();

                            // Make request.
                            Response response = asyncHttpClient.executeRequest(request).get();

                            // Calculate service response time.
                            difference = System.nanoTime() - start;

                            // Log results.
                            results[resultsOffset + i][0] = difference;
                            results[resultsOffset + i][1] = response.getStatusCode();
                            results[resultsOffset + i][2] = System.currentTimeMillis();

                            // Wait to execute next request.
                            LockSupport.parkNanos(nanosBetweenRequests - difference);

                        } catch (InterruptedException | ExecutionException e) {
                            logger.error("Issue connecting to {} with method {}: {}", host, method, e.getMessage());
                            results[resultsOffset + i][0] = -1;
                            results[resultsOffset + i][1] = -1;
                            results[resultsOffset + i][2] = System.currentTimeMillis();
                        }
                    }
                }
            });
        }

        // Await termination of all jobs.
        logger.info("Awaiting load test completion.");
        boolean status = fjp.awaitQuiescence(2 * seconds, TimeUnit.SECONDS);
        logger.info("Load test completion status: {}", status);

        // Return results.
        return results;
    }

    public static void main(String[] args) {

        // Validate arguments.
        if (args.length < 7) {
            throw new IllegalArgumentException("Expected 7 arguments:\n" +
                                               "- Fully qualified name of service endpoint to target (e.g., http://localhost:3344/hello)\n" +
                                               "- Name of the method to use against the service endpoint (e.g., GET or POST)\n" +
                                               "- Payload to use against the service endpoint if the method is POST. Pass null if no payload or not a POST request.\n" +
                                               "- Filename (without extension) to save on test completion.\n" +
                                               "- Number of workers to use.\n" +
                                               "- Number of requests for each worker to make per second.\n" +
                                               "- Number of seconds to run for.\n");
        }

        // Warm up service with 1K requests.
        logger.info("Warming up service with 5K requests over 10 seconds.");
        applyLoad(args[0], args[1], args[2], 1, 500, 10);

        // Run load test.
        logger.info("Beginning load test.");
        long[][] results;
        results = applyLoad(args[0], args[1], args[2], Integer.valueOf(args[4]), Integer.valueOf(args[5]), Integer.valueOf(args[6]));

        // Sort results by UNIX timestamp.
        logger.info("Sorting results.");
        Arrays.sort(results, new Comparator<long[]>() {
            @Override
            public int compare(final long[] entry1, final long[] entry2) {
                return Long.compare(entry1[2], entry2[2]);
            }
        });

        // Report median data.
        if (results.length % 2 == 0) {
            logger.info("Median Response Time (NS): {}", ((double) results[results.length / 2][2] + (double) results[results.length / 2 - 1][2]) / 2);
        } else {
            logger.info("Median Response Time (NS): {}", ((double) results[results.length / 2][2]));
        }

        // Generate CSV data.
        logger.info("Generating CSV data.");
        StringBuilder csv = new StringBuilder();
        // csv.append("Latency (Microseconds),Status,Timestamp,Index\n");
        int i = 0;
        for (long[] result : results) {
            csv.append(result[0] / 1000).append(",");
            csv.append(result[1]).append(",");
            csv.append(result[2]).append(",");
            csv.append(i).append("\n");
            i++;
        }

        // Write CSV data to file.
        logger.info("Writing CSV data to file.");
        Path file = Paths.get(args[3] + ".csv");
        try {
            Files.write(file, csv.toString().getBytes(Charset.forName("UTF-8")));
        } catch (IOException e) {
            logger.error("Error writing CSV to file {}: {}", file.getFileName().toString(), e.getMessage(), e);
        }
    }
}