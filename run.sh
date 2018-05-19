#!/bin/bash

# Prevent SIGTERM from ending execution of tests.
trap "echo Service attempted a SIGTERM." SIGTERM SIGKILL

for BITS in 0 2 4 6 8 10; do

    # Node
    java -Xms10g -jar target/green-loader.jar -f load-configs/node.json -b $BITS
    java -Xms10g -jar target/green-loader.jar -f load-configs/node-logging.json -b $BITS
    
    # Jetty
    java -Xms10g -jar target/green-loader.jar -f load-configs/jetty.json -b $BITS
    java -Xms10g -jar target/green-loader.jar -f load-configs/jetty-logging.json -b $BITS
    
    # Tomcat
    java -Xms10g -jar target/green-loader.jar -f load-configs/tomcat.json -b $BITS
    java -Xms10g -jar target/green-loader.jar -f load-configs/tomcat-logging.json -b $BITS

    # Netty
    java -Xms10g -jar target/green-loader.jar -f load-configs/netty.json -b $BITS

    # Pronghorn
    # TODO: Re-enable when Pronghorn app is fixed.
    # java -Xms10g -jar target/green-loader.jar -f load-configs/ph.json -b $BITS
    # java -Xms10g -jar target/green-loader.jar -f load-configs/phl.json -b $BITS
    # java -Xms10g -jar target/green-loader.jar -f load-configs/pht.json -b $BITS
    # java -Xms10g -jar target/green-loader.jar -f load-configs/phlt.json -b $BITS
    
    # Green Lightning
    java -Xms10g -jar target/green-loader.jar -f load-configs/gl.json -b $BITS
    java -Xms10g -jar target/green-loader.jar -f load-configs/gll.json -b $BITS
    java -Xms10g -jar target/green-loader.jar -f load-configs/glt.json -b $BITS
    java -Xms10g -jar target/green-loader.jar -f load-configs/gllt.json -b $BITS

    # PHP
    java -Xms10g -jar target/green-loader.jar -f load-configs/php.json -b $BITS
    # Note: For PHP logging, we MUST delete its logs between runs; it just creates too much data.
    rm -rf php/logs/error_log
    java -Xms10g -jar target/green-loader.jar -f load-configs/php-logging.json -b $BITS
    
    # Micronaut
    java -Xms10g -jar target/green-loader.jar -f load-configs/micronaut.json -b $BITS

    # Play!
    java -Xms10g -jar target/green-loader.jar -f load-configs/play.json -b $BITS
   
done