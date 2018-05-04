#!/bin/bash

# The load tester.
mvn clean package

# Node.
npm install body-parser
npm install express
npm install morgan

# Jetty
cd spring-boot-jetty
mvn clean package
cd ..

# Tomcat
cd spring-boot-tomcat
mvn clean package
cd ..

# Green Lightning
cd green-lightning
mvn clean package
cd ..

# Micronaut
cd micronaut-app
./gradlew clean shadowJar
cd ..

# Squall
# cd squall
# mvn clean package
# cd ..