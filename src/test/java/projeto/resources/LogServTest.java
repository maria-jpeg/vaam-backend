package projeto.resources;


import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;


import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import projeto.NCFinderApplication;
import projeto.NCFinderConfiguration;
import projeto.api.dtos.entities.LogDTO;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


@ExtendWith(DropwizardExtensionsSupport.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LogServTest
{

    private static final String TEST_RESOURCES_PATH = new File( "src/test/resources" ).getAbsolutePath();

    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("config_test.yml");

    public static final DropwizardAppExtension<NCFinderConfiguration> RULE = new DropwizardAppExtension<>(
            NCFinderApplication.class, CONFIG_PATH
            );


    @Test
    @Order(1)
    void testAPI()
    {
        int localPort = RULE.getLocalPort();
        assertTrue( localPort != 0 );
    }


    @Test
    void CRUDCSVLog()
    {
        int numberOfLogs = getLogs().size();

        LogDTO log = createLog(
                TEST_RESOURCES_PATH + "/logs_test/PurchasingExample.csv",
                "Log CSV Test.");

        // Check if log was added
        assertEquals( numberOfLogs + 1 , getLogs().size() );

        // Check if log added equals to log in data base
        checkLog( log );

        // Update Description
        log.setDescription( "New Description" );
        log = updateLog( log );
        checkLog( log );

        // Delete log
        deleteLog( log.getId() );

        // Check if log was removed
        assertEquals( numberOfLogs, getLogs().size() );

    }

    @Test
    void CRUDXESLog()
    {
        int numberOfLogs = getLogs().size();

        LogDTO log = createLog(
                TEST_RESOURCES_PATH + "/logs_test/PurchasingExample.XES",
                "Log CSV Test.");

        // Check if log was added
        assertEquals( numberOfLogs + 1 , getLogs().size() );

        // Check if log added equals to log in data base
        checkLog( log );

        // Update Description
        log.setDescription( "New Description" );
        log = updateLog( log );
        checkLog( log );

        // Delete log
        deleteLog( log.getId() );

        // Check if log was removed
        assertEquals( numberOfLogs, getLogs().size() );

    }

    @Test
    void invalidLogFileExtension()
    {

        try
        {
            createLog(
                    TEST_RESOURCES_PATH + "/config_test.yml",
                    "Log invalid extension.");
        }
        catch (RuntimeException e) { return; }

        fail("expected Exception for invalid extension log file.");
    }

    @Test
    void invalidLogFile()
    {

        try
        {
            createLog(
                    TEST_RESOURCES_PATH + "/logs_test/PurchasingExample_invalid.csv",
                    "Invalid log file.");
        }
        catch (RuntimeException e) { return; }

        fail("expected Exception for invalid log file.");
    }


    //==================================================================================================================


    void checkLog(LogDTO createdLog )
    {
        LogDTO log = getLog( createdLog.getId() );

        assertEquals( createdLog.getFilename(), log.getFilename() );
        assertEquals( createdLog.getDescription(), log.getDescription() );
        assertEquals( createdLog.getStartDate(), log.getStartDate() );
        assertEquals( createdLog.getEndDate(), log.getEndDate() );
        assertEquals( createdLog.getNumberOfActivities(), log.getNumberOfActivities() );
        assertEquals( createdLog.getNumberOfCases(), log.getNumberOfCases() );
    }

    private LogDTO createLog(String path, String description )
    {

        FileDataBodyPart fileBodyPart = new FileDataBodyPart( "file", new File(path) );

        MultiPart multipartEntity = new FormDataMultiPart()
                .field("description", description )
                .bodyPart( fileBodyPart );

        RULE.client().register( MultiPartFeature.class );

        Response response = RULE.client()
                .target( String.format("http://localhost:%d/api/logs", RULE.getLocalPort()) )
                .request( MediaType.APPLICATION_JSON )
                .post( Entity.entity(multipartEntity, multipartEntity.getMediaType()) );


        int status = response.getStatus();
        if( status == Response.Status.CREATED.getStatusCode() )
        {
            LogDTO created = response.readEntity( LogDTO.class );
            response.close();
            return created;
        }

        throw new RuntimeException( response.readEntity( String.class ) );

    }

    private LogDTO getLog( Long id )
    {

        return RULE.client()
                .target( String.format("http://localhost:%d/api/logs/%d", RULE.getLocalPort(), id) )
                .request( MediaType.APPLICATION_JSON )
                .get( LogDTO.class );

    }


    List<LogDTO> getLogs()
    {
        /*
        Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");

        Response response = client.target(
                String.format("http://localhost:%d/api/logs", RULE.getLocalPort()))
                .request()
                .get();

        Object entity = response.getEntity();
        */


        Response response = RULE.client()
                .target( String.format("http://localhost:%d/api/logs", RULE.getLocalPort()) )
                .request( MediaType.APPLICATION_JSON )
                .get();


        List<LogDTO> logs = response.readEntity( new GenericType<List<LogDTO>>() {} );
        response.close();

        return logs;

    }

    private LogDTO updateLog( LogDTO log )
    {
        return RULE.client()
                .target( String.format("http://localhost:%d/api/logs", RULE.getLocalPort()) )
                .request( MediaType.APPLICATION_JSON )
                .put( Entity.entity(log, MediaType.APPLICATION_JSON_TYPE) )

                .readEntity( LogDTO.class );

    }

    private LogDTO deleteLog( Long id )
    {
        return RULE.client()
                .target( String.format("http://localhost:%d/api/logs/%d", RULE.getLocalPort(), id) )
                .request( MediaType.APPLICATION_JSON )
                .delete( LogDTO.class );

    }


}