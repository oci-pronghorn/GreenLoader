#!/bin/bash

# Prevent SIGTERM from ending execution of tests.
trap "echo Service attempted a SIGTERM." SIGTERM SIGKILL

for TRACKS in 1 2 4; do

    # Node
    java -Xms10g -jar target/green-loader.jar -f load-configs/node.json -b 6 -t $TRACKS
    java -Xms10g -jar target/green-loader.jar -f load-configs/node-logging.json -b 6 -t $TRACKS
    
    # Jetty
    java -Xms10g -jar target/green-loader.jar -f load-configs/jetty.json -b 6 -t $TRACKS
    java -Xms10g -jar target/green-loader.jar -f load-configs/jetty-logging.json -b 6 -t $TRACKS
    
    # Tomcat
    java -Xms10g -jar target/green-loader.jar -f load-configs/tomcat.json -b 6 -t $TRACKS
    java -Xms10g -jar target/green-loader.jar -f load-configs/tomcat-logging.json -b 6 -t $TRACKS

    # Netty
    java -Xms10g -jar target/green-loader.jar -f load-configs/netty.json -b 6 -t $TRACKS

    # Pronghorn
    java -Xms10g -jar target/green-loader.jar -f load-configs/ph.json -b 6 -t $TRACKS
    java -Xms10g -jar target/green-loader.jar -f load-configs/phl.json -b 6 -t $TRACKS
    java -Xms10g -jar target/green-loader.jar -f load-configs/pht.json -b 6 -t $TRACKS
    java -Xms10g -jar target/green-loader.jar -f load-configs/phlt.json -b 6 -t $TRACKS
    
    # Green Lightning
    java -Xms10g -jar target/green-loader.jar -f load-configs/gl.json -b 6 -t $TRACKS
    java -Xms10g -jar target/green-loader.jar -f load-configs/gll.json -b 6 -t $TRACKS
    java -Xms10g -jar target/green-loader.jar -f load-configs/glt.json -b 6 -t $TRACKS
    java -Xms10g -jar target/green-loader.jar -f load-configs/gllt.json -b 6 -t $TRACKS

    # PHP
    java -Xms10g -jar target/green-loader.jar -f load-configs/php.json -b 6 -t $TRACKS
    # Note: For PHP logging, we MUST delete its logs between runs; it just creates too much data.
    rm -rf php/logs/error_log
    java -Xms10g -jar target/green-loader.jar -f load-configs/php-logging.json -b 6 -t $TRACKS
    
    # Micronaut
    java -Xms10g -jar target/green-loader.jar -f load-configs/micronaut.json -b 6 -t $TRACKS

    # Play!
    java -Xms10g -jar target/green-loader.jar -f load-configs/play.json -b 6 -t $TRACKS

done