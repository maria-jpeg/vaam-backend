#!/bin/sh
java "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8787" -jar target/ncfinder-1.0.0-SNAPSHOT.jar server config.yml