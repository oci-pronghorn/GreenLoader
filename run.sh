#!/bin/bash

# Node
java -Xms6g -jar target/green-loader.jar node.json
java -Xms6g -jar target/green-loader.jar node-logging.json

# Jetty
java -Xms6g -jar target/green-loader.jar jetty.json
java -Xms6g -jar target/green-loader.jar jetty-logging.json

# Tomcat
java -Xms6g -jar target/green-loader.jar tomcat.json
java -Xms6g -jar target/green-loader.jar tomcat-logging.json

# Green Lightning
java -Xms6g -jar target/green-loader.jar gl.json
java -Xms6g -jar target/green-loader.jar gll.json
java -Xms6g -jar target/green-loader.jar glt.json
java -Xms6g -jar target/green-loader.jar gltl.json

# Micronaut
# java -Xms6g -jar target/green-loader.jar micronaut.json

# Squall
# java -Xms6g -jar target/green-loader.jar squall.json