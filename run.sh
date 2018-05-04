#!/bin/bash

# Node
java -Xms10g -jar target/green-loader.jar load-configs/node.json
java -Xms10g -jar target/green-loader.jar load-configs/node-logging.json

# Jetty
java -Xms10g -jar target/green-loader.jar load-configs/jetty.json
java -Xms10g -jar target/green-loader.jar load-configs/jetty-logging.json

# Tomcat
java -Xms10g -jar target/green-loader.jar load-configs/tomcat.json
java -Xms10g -jar target/green-loader.jar load-configs/tomcat-logging.json

# Green Lightning
java -Xms10g -jar target/green-loader.jar load-configs/gl.json
java -Xms10g -jar target/green-loader.jar load-configs/gll.json
java -Xms10g -jar target/green-loader.jar load-configs/glt.json
java -Xms10g -jar target/green-loader.jar load-configs/gltl.json

# Micronaut
# java -Xms10g -jar target/green-loader.jar load-configs/micronaut.json

# Squall
# java -Xms6g -jar target/green-loader.jar squall.json