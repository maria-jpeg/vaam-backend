#
# Build stage
#
FROM maven:3.6.0-jdk-11 AS build
COPY . /home/app
COPY docker/app/dockerfile-app.sh /home/app/dockerfile-app.sh
RUN chmod +x /home/app/dockerfile-app.sh

WORKDIR /home/app
#
# Package stage
#
EXPOSE 8080
ENTRYPOINT ["/bin/sh","dockerfile-app.sh"]