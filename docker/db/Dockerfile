FROM mcr.microsoft.com/mssql/server

ENV ACCEPT_EULA=Y
ENV SA_PASSWORD=Your_password123

COPY ./docker/db /
#RUN chmod +x dockerfile-db.sh

ENTRYPOINT [ "/bin/bash", "dockerfile-db.sh" ]
CMD [ "/opt/mssql/bin/sqlservr" ]