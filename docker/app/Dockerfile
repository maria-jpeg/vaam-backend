#
# Build stage
#

FROM maven:3.6.0-jdk-11 AS maven_build

WORKDIR /home/app
COPY libs/ ./libs
COPY lib/ ./lib
COPY pom.xml .

COPY src ./src
COPY config.yml .

COPY docker/app/dockerfile-app.sh .
RUN chmod +x dockerfile-app.sh

#
#   RUN STAGE
#
EXPOSE 8080
ENTRYPOINT ["/bin/sh","dockerfile-app.sh"]