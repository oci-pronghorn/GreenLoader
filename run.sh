#!/bin/bash

# Node
java -Xms6g -jar target/green-loader.jar node.json

# Jetty
java -Xms6g -jar target/green-loader.jar jetty.json

# Tomcat
java -Xms6g -jar target/green-loader.jar tomcat.json

# Green Lightning
java -Xms6g -jar target/green-loader.jar glt.json
java -Xms6g -jar target/green-loader.jar gl.json

# Micronaut
# java -Xms6g -jar target/green-loader.jar micronaut.json

# Squall
# java -Xms6g -jar target/green-loader.jar squall.json