#!/bin/bash
set -e
if [ "$1" = '/opt/mssql/bin/sqlservr' ]; then
  # If this is the container's first run, initialize the application database
  if [ ! -f /tmp/app-initialized ]; then
    # Initialize the application database asynchronously in a background process. This allows a) the SQL Server process to be the main process in the container, which allows graceful shutdown and other goodies, and b) us to only start the SQL Server process once, as opposed to starting, stopping, then starting it again.
    function initialize_app_database() {
      # Wait a bit for SQL Server to start. SQL Server's process doesn't provide a clever way to check if it's up or not, and it needs to be up before we can import the application database
      sleep 15s
      #run the setup script to create the DB and the schema in the DB
      /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Your_password123 -d master -i setup.sql
      /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Your_password123 -d master -i data/1_ROLES.sql
      #running-example
      /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Your_password123 -d master -i data/running-example/2_USERS.sql
      /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Your_password123 -d master -i data/running-example/3_PROCESSES.sql
      /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Your_password123 -d master -i data/running-example/4_ACTIVITIES.sql
      /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Your_password123 -d master -i data/running-example/5_MOULDS.sql
      /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Your_password123 -d master -i data/running-example/6_WORKSTATIONS.sql
      /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Your_password123 -d master -i data/running-example/7_EVENTS.sql
      /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Your_password123 -d master -i data/running-example/8_ACTIVITIES_USERS.sql
      #running-example-just-two-cases
      /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Your_password123 -d master -i data/running-example-just-two-cases/3_PROCESSES.sql
      /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Your_password123 -d master -i data/running-example-just-two-cases/7_EVENTS.sql
      /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Your_password123 -d master -i data/running-example-just-two-cases/8_ACTIVITIES_USERS.sql
      #running-example-non-conforming
      /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Your_password123 -d master -i data/running-example-non-conforming/3_PROCESSES.sql
      /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Your_password123 -d master -i data/running-example-non-conforming/5_MOULDS.sql
      /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Your_password123 -d master -i data/running-example-non-conforming/7_EVENTS.sql
      /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Your_password123 -d master -i data/running-example-non-conforming/8_ACTIVITIES_USERS.sql
      # Note that the container has been initialized so future starts won't wipe changes to the data
      touch /tmp/app-initialized
    }
    initialize_app_database &
  fi
fi
exec "$@"