#!/bin/sh
mvn clean install -U && java -jar target/ncfinder-1.0.0-SNAPSHOT.jar server config.yml && ls