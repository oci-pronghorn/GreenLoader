#!/bin/bash

# Prevent SIGTERM from ending execution of tests.
trap "echo Service attempted a SIGTERM." SIGTERM SIGKILL

for BITS in 0 2 4 6 8 10; do

    # Node
    java -Xms10g -jar target/green-loader.jar -f load-configs/node.json -b $BITS && sleep 10
    java -Xms10g -jar target/green-loader.jar -f load-configs/node-logging.json -b $BITS && sleep 10
    
    # Jetty
    java -Xms10g -jar target/green-loader.jar -f load-configs/jetty.json -b $BITS && sleep 10
    java -Xms10g -jar target/green-loader.jar -f load-configs/jetty-logging.json -b $BITS && sleep 10
    
    # Tomcat
    java -Xms10g -jar target/green-loader.jar -f load-configs/tomcat.json -b $BITS && sleep 10
    java -Xms10g -jar target/green-loader.jar -f load-configs/tomcat-logging.json -b $BITS && sleep 10

    # Netty
    java -Xms10g -jar target/green-loader.jar -f load-configs/netty.json -b $BITS && sleep 10

    # Pronghorn
    java -Xms10g -jar target/green-loader.jar -f load-configs/ph.json -b $BITS && sleep 10
    java -Xms10g -jar target/green-loader.jar -f load-configs/phl.json -b $BITS && sleep 10
    java -Xms10g -jar target/green-loader.jar -f load-configs/pht.json -b $BITS && sleep 10
    java -Xms10g -jar target/green-loader.jar -f load-configs/phlt.json -b $BITS && sleep 10
    
    # Green Lightning
    java -Xms10g -jar target/green-loader.jar -f load-configs/gl.json -b $BITS && sleep 10
    java -Xms10g -jar target/green-loader.jar -f load-configs/gll.json -b $BITS && sleep 10
    java -Xms10g -jar target/green-loader.jar -f load-configs/glt.json -b $BITS && sleep 10
    java -Xms10g -jar target/green-loader.jar -f load-configs/gllt.json -b $BITS && sleep 10

    # PHP
    java -Xms10g -jar target/green-loader.jar -f load-configs/php.json -b $BITS && sleep 10
    # Note: For PHP logging, we MUST delete its logs between runs; it just creates too much data.
    rm -rf php/logs/error_log
    java -Xms10g -jar target/green-loader.jar -f load-configs/php-logging.json -b $BITS && sleep 10
    
    # Micronaut
    java -Xms10g -jar target/green-loader.jar -f load-configs/micronaut.json -b $BITS && sleep 10

    # Play!
    java -Xms10g -jar target/green-loader.jar -f load-configs/play.json -b $BITS && sleep 10

done