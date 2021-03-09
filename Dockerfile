#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY . /home/app
COPY dockerfile-cmd.sh /opt/dockerfile-cmd.sh
RUN chmod +x /opt/dockerfile-cmd.sh
WORKDIR /home/app
#
# Package stage
#
EXPOSE 8080
ENTRYPOINT ["/bin/sh","/opt/dockerfile-cmd.sh"]