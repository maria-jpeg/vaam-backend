#!/bin/bash
cd /home/app ; mvn clean install -U
java -jar /home/app/target/ncfinder-1.0.0-SNAPSHOT.jar server /home/app/config.yml