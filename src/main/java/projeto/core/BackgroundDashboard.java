package projeto.core;

import org.knowm.sundial.Job;
import org.knowm.sundial.annotations.SimpleTrigger;
import org.knowm.sundial.exceptions.JobInterruptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@SimpleTrigger(repeatInterval = 5, timeUnit = TimeUnit.SECONDS)
public class BackgroundDashboard  extends Job {

    private final Logger logger = LoggerFactory.getLogger(BackgroundDashboard.class);

    @Override
    public void doRun() throws JobInterruptException {
        System.out.println("teste");
        logger.info("myValue: " );

    }

    private void selectProcess(){

    }
}
