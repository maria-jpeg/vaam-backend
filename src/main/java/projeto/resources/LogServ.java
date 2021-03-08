package projeto.resources;


import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import projeto.api.dtos.entities.LogDTO;
import projeto.controller.LogBean;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.EntityExistsException;
import projeto.controller.exceptions.InvalidLogException;
import projeto.controller.exceptions.ParsingException;
import projeto.core.Log;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

//@SwaggerDefinition(tags = { @Tag(name = "logs", description = "Operations about event logs") })
//@Api( value = "logs")
//@Path("logs")
public class LogServ extends CRUDService<Log, LogDTO, Long>
{
    private final LogBean logBean;

    public LogServ(LogBean logBean) {
        super(logBean);
        this.logBean = logBean;
    }
    /*
    //region //Swagger Annotations
    @ApiOperation( value = "Get all Logs", response = LogDTO.class, responseContainer = "List")
    public Response getAllTags() { return super.getAllTags(); }

    @ApiOperation( value = "Get a Log by id", response = LogDTO.class)  @ApiResponses(value = { @ApiResponse(code = 404, message = "Log not found") })
    public Response getById(Long id) { return super.getById(id); }

    @ApiOperation( value = "Delete a Log by id") @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return deleted log", response = LogDTO.class), @ApiResponse(code = 404, message = "Log not found") })
    public Response delete(Long id) { return super.delete(id); }

    @ApiOperation( value = "Update a Log by id") @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return updated log", response = LogDTO.class), @ApiResponse(code = 404, message = "Log not found") })
    public Response update(LogDTO dto) { return super.update(dto); }
    //endregion

    @ApiOperation( value = "Create a new Log", response = LogDTO.class)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Return created log", response = LogDTO.class)})
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({ MediaType.APPLICATION_JSON})
    @UnitOfWork
    public Response create(
            @NotNull(message = "- file required")
            @FormDataParam("file") InputStream uploadedInputStream,
            @NotNull(message = "- file required")
            @FormDataParam("file") FormDataContentDisposition fileDetail,

            @NotNull(message = "- description required")
            @Size(min = 3, max = 255, message = "- description must be between {min} and {max} characters")
            @FormDataParam("description") String description) {

        String filename = fileDetail.getFileName();

        try {
            Log log = logBean.create( uploadedInputStream, filename, description );
            if( log == null )
                return Response.status(Response.Status.CONFLICT).entity( "Log is null" ).build();

            log.setEvents( null );

            return Response.status(Response.Status.CREATED)
                    .entity( logBean.getDao().toDTO( log ) ).build();
        } catch (ParsingException | EntityExistsException e) {
            e.printStackTrace();
            return Response.status(Response.Status.CONFLICT)
                    .entity( e.getMessage() ) .build();
        }
        catch (InvalidLogException e)
        {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST) .type( MediaType.TEXT_PLAIN )
                    .entity( e.getMessage() ) .build();
        }

    }

    @Override
    public Response create(LogDTO dto) { return Response.status(Response.Status.BAD_REQUEST).build(); }


    @ApiOperation( value = "Get the activities of a log", response = String.class, responseContainer = "List")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @UnitOfWork
    @Path("{id}/activities")
    public Response getActivitiesByLogID( @PathParam("id") long logID )
    {
        try {
            return Response.ok( logBean.getActivitiesfromLogID( logID ) )
                   .build();

        }
        catch( EntityDoesNotExistException ex )
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
        catch (Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
    }

    @ApiOperation( value = "Get the resources of a log", response = String.class, responseContainer = "List")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @UnitOfWork
    @Path("{id}/resources")
    public Response getResourcesByLogID( @PathParam("id") long logID )
    {
        try {
            return Response.ok( logBean.getResourcesfromLogID( logID ) )
                    .build();

        }
        catch( EntityDoesNotExistException ex )
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
        catch (Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
    }




/*
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @UnitOfWork
    @Path("teste")
    public Response teste(
           )
            //  @QueryParam("startDate") String startDateStr, @QueryParam("startDate") String endDateStr

    {

        String startDateStr = "1-1-2011";
        String endDateStr = "1-1-2011";

        Pair<DateTime, DateTime> datesPair = Utils.parseDateBetween(startDateStr, endDateStr);

        DateTime startDate = datesPair.getFirst();
        DateTime endDate = datesPair.getSecond();

        System.out.println( "start: " + startDate );
        System.out.println( "end: " + endDate );

        try {
            List<Event> events = logBean.getEventsBetweenDatesByLogID(1,
                    startDate,
                    endDate );

            for (Event e : events) {
                System.out.println( e.getEnd().toString( "dd-MM-yyyy HH:mm:SSS" ) );
            }

            return Response.ok( "OK" )
                    .build();

        } catch (Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
    }

    */

}

