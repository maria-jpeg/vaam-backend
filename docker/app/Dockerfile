FROM ubuntu:18.04

WORKDIR /usr/lib

#wget dos ficheiros para lpsolve
RUN apt update -y && \
    apt install openjdk-11-jdk -y && \
    apt install wget -y && \
    wget "https://svn.win.tue.nl/repos/prom/Packages/LpSolve/LpSolve_lin64/trunk/lib/ux64/liblpsolve55.so" && \
    wget "https://svn.win.tue.nl/repos/prom/Packages/LpSolve/LpSolve_lin64/trunk/lib/ux64/liblpsolve55j.so" && \
    ldconfig

WORKDIR /home/app

COPY docker/app/dockerfile-app.sh .
RUN chmod +x dockerfile-app.sh


EXPOSE 8080
EXPOSE 8787
ENTRYPOINT ["/bin/sh","dockerfile-app.sh"]