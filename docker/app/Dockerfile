FROM ubuntu:18.04

WORKDIR /usr/lib

#wget dos ficheiros para lpsolve
RUN apt update -y && \
    apt install openjdk-11-jdk -y && \
    apt install locales -y && \
    apt install wget -y && \
    wget "https://svn.win.tue.nl/repos/prom/Packages/LpSolve/LpSolve_lin64/trunk/lib/ux64/liblpsolve55.so" && \
    wget "https://svn.win.tue.nl/repos/prom/Packages/LpSolve/LpSolve_lin64/trunk/lib/ux64/liblpsolve55j.so" && \
    ldconfig

# Set the locale -> https://stackoverflow.com/questions/28405902/how-to-set-the-locale-inside-a-debian-ubuntu-docker-container
RUN touch /usr/share/locale/locale.alias
RUN sed -i '/en_US.UTF-8/s/^# //g' /etc/locale.gen && locale-gen
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

WORKDIR /home/app

COPY docker/app/dockerfile-app.sh .
RUN chmod +x dockerfile-app.sh


EXPOSE 8080
EXPOSE 8787
ENTRYPOINT ["/bin/sh","dockerfile-app.sh"]