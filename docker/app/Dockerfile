FROM openjdk:11-oracle
WORKDIR /home/app

COPY docker/app/dockerfile-app.sh .
RUN chmod +x dockerfile-app.sh


EXPOSE 8080
EXPOSE 8787
ENTRYPOINT ["/bin/sh","dockerfile-app.sh"]