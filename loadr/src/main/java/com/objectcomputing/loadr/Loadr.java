package com.objectcomputing.loadr;

import java.lang.Exception;
import java.lang.Thread;
import java.util.concurrent.Future;
import java.util.Arrays;

import com.ning.http.client.*;

public class Loadr {
    public static void main(String[] args) {
        long start, stop = 0;
        long[] numArray = new long[1000];

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        AsyncHttpClient.BoundRequestBuilder request = asyncHttpClient.prepareGet("http://localhost:8000");

        for (int i = 0; i < 1000; i++) {
            try {
                start = System.nanoTime();
                Future<Response> f = request.execute();
                stop = System.nanoTime();
                numArray[i] = stop - start;
                // Response r = f.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Arrays.sort(numArray);
        double median;
        if (numArray.length % 2 == 0) {
            median = ((double)numArray[numArray.length/2] + (double)numArray[numArray.length/2 - 1])/2;
        } else {
            median = (double) numArray[numArray.length/2];
        }

        System.out.println("Median Performance: " + median);
    }
}