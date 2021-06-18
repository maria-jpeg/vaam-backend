package projeto.job;

import io.dropwizard.hibernate.UnitOfWork;
import lombok.SneakyThrows;
import org.hibernate.SessionFactory;
import org.knowm.sundial.Job;
import org.knowm.sundial.SundialJobScheduler;
import org.knowm.sundial.annotations.CronTrigger;
import org.knowm.sundial.annotations.SimpleTrigger;
import org.knowm.sundial.exceptions.JobInterruptException;

import projeto.controller.ProcessBean;
import projeto.core.Process;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

@CronTrigger(cron = "0 1 0 * * ?")
public class BackgroundDashboard extends Job {

    private ProcessBean processBean;


    @SneakyThrows
    @Override
    public void doRun() throws JobInterruptException {
        System.out.println("Start");

        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL("http://localhost:8080/api/dashboard/generate");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("accept", "application/json");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("response.toString()");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }


        System.out.println("End");

    }

    @UnitOfWork
    private void Start() {
        System.out.println("Middle");
    }

}
