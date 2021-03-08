#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY . /home/app
COPY dockerfile-cmd.sh /opt/dockerfile-cmd.sh
RUN chmod +x /opt/dockerfile-cmd.sh

#RUN mvn -f /home/app/pom.xml clean package
#RUN cd /home/app ; ls
#RUN cd /home/app ; mvn install -U

#
# Package stage
#
#FROM openjdk:11-jre-slim
#COPY --from=build /home/app/target/ncfinder-0.0.1-SNAPSHOT.jar /usr/local/lib/ncfinder.jar
EXPOSE 8080
ENTRYPOINT ["/bin/sh","/opt/dockerfile-cmd.sh"]