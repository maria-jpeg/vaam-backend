package projeto.job;

import lombok.SneakyThrows;
import org.knowm.sundial.Job;
import org.knowm.sundial.annotations.CronTrigger;
import org.knowm.sundial.annotations.SimpleTrigger;
import org.knowm.sundial.exceptions.JobInterruptException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/*
* For testing
* @SimpleTrigger(repeatInterval = 30, timeUnit = TimeUnit.SECONDS)
*/
//@CronTrigger(cron = "0 1 0 * * ?")
@SimpleTrigger(repeatInterval = 30, timeUnit = TimeUnit.SECONDS)
public class BackgroundDashboard extends Job {


    @SneakyThrows
    @Override
    public void doRun() throws JobInterruptException {
        //HTTP Request get token
        String token = getToken();

        //HTTP Request for generate dashboard
        generate(token);

    }

    private String getToken() {
        HttpURLConnection connectionLogin = null;
        String token = null;

        try {
            //Create connection
            URL url = new URL("http://localhost:8080/api/login/token");
            connectionLogin = (HttpURLConnection) url.openConnection();
            connectionLogin.setRequestMethod("POST");
            connectionLogin.setRequestProperty("Content-Type", "application/json");
            connectionLogin.setRequestProperty("accept", "application/json");
            connectionLogin.setDoOutput(true);

            String data = "{ \"username\": \"job\", \"password\": \"123\"}";

            byte[] out = data.getBytes();

            OutputStream stream = connectionLogin.getOutputStream();
            stream.write(out);

            //Get Response
            InputStream is1 = connectionLogin.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is1));
            StringBuilder responseLogin = new StringBuilder(); // or StringBuffer if Java version 5+
            String line1;
            while ((line1 = rd.readLine()) != null) {
                responseLogin.append(line1);
                responseLogin.append('\r');
            }
            rd.close();

            String[] cleanResponse = responseLogin.toString().split(":");
            token = cleanResponse[1].substring(1,cleanResponse[1].length()-3);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error");
            System.out.println(e);
        } finally {
            if (connectionLogin != null) {
                connectionLogin.disconnect();
            }
        }
        return token;
    }

    private void generate(String token){
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL("http://localhost:8080/api/dashboard/generate");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer "+token);

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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}
