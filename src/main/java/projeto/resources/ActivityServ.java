package projeto.resources;


import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import projeto.api.dtos.FrequencyDTO;
import projeto.api.dtos.compare.FilterInputWrapperDTO;
import projeto.api.dtos.resources.operationalhours.ActivityTotalOperationalHoursDTO;
import projeto.api.dtos.resources.operationalhours.ActivityWorkstationsDTO;
import projeto.api.dtos.resources.workhours.ActivityTotalWorkHoursDTO;
import projeto.api.dtos.resources.workhours.ActivityUsersDTO;
import projeto.controller.ActivityBean;
import projeto.controller.LogBean;
import projeto.controller.ProcessBean;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.MyException;
import projeto.core.Event;
import projeto.core.extra.FilterWrapper;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;


@SwaggerDefinition(tags = { @Tag(name = "activities", description = "Activities statistics") })
@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ") })
@Api(value="activities")
@Path("activities")
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class ActivityServ
{

    private final ActivityBean activityBean;
    private final LogBean logBean;
    private final ProcessBean processBean;

    public ActivityServ(ActivityBean activityBean, LogBean logBean, ProcessBean processBean )
    {
        this.activityBean = activityBean;
        this.logBean = logBean;
        this.processBean = processBean;
    }
/*
    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    public Response root() { return csv(); }

    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    @Path("csv")
    public Response csv()
    {
        try {

            List<FrequencyDTO> frequencies = activityBean.getFrequenciesFromCSV( "./src/main/resources/PurchasingExample.csv" );
            return Response.ok( frequencies ).build();

        }
        catch (Exception ex)
        {

            return Response.status(Response.Status.CONFLICT) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
            //return null;
        }

    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    @Path("xes")
    public Response xes()
    {
        try {

            List<FrequencyDTO> frequencies = activityBean.getFrequenciesFromXES( "./src/main/resources/PurchasingExample.xes" );
            return Response.ok( frequencies ).build();

        }
        catch (Exception ex)
        {

            return Response.status(Response.Status.CONFLICT) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
            //return null;
        }

    }
*/

    /*
    @ApiOperation( value = "testing...", response = FrequencyDTO.class, responseContainer = "List")
    @ApiImplicitParam(name = "user", value = "Authentication injection")
    @PermitAll
    @GET
    @Path( "secure" )
    @Produces({ MediaType.APPLICATION_JSON})
    public Response root( @ApiParam(hidden = true) @Auth User user )
    {
        return Response.ok( "Secure!" ).build();
    }
    */

    /*@ApiOperation( value = "Get activities frequency of log", response = FrequencyDTO.class, responseContainer = "List")
    @GET
    @UnitOfWork
    @Produces({ MediaType.APPLICATION_JSON})
    @Path("{id}")
    public Response getActivityFrequency(@PathParam("id") long id )
    {

        //long startTime = System.currentTimeMillis();
        try {
            List<Event> events = logBean.getEventsByLogID( id );

            List<FrequencyDTO> frequencies = activityBean.computeFrequency( events );
            return Response.ok( frequencies ).build();

        }
        catch ( EntityDoesNotExistException ex)
        {

            return Response.status(Response.Status.CONFLICT) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
            //return null;
        }

    }*/

    @ApiOperation( value = "Get activities frequency and average duration of process", response = FrequencyDTO.class, responseContainer = "List")
    @GET
    @UnitOfWork
    @Path("{processId}/freqduration")
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getActivityFrequencyDuration(@PathParam("processId") long processId )
    {

        try {
            List<Event> events = processBean.getEventsByProcessID( processId );

            List<FrequencyDTO> frequencies = activityBean.computeFrequency( events );
            return Response.ok( frequencies ).build();

        }
        catch ( EntityDoesNotExistException ex)
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }

    }


/*    @ApiOperation( value = "Get average effort (total resource time) spent to perform each activity of a process", response = FrequencyDTO.class, responseContainer = "List")
    @GET
    @UnitOfWork
    @Path("effort/{processId}")
    public Response getActivityFrequencyEffort(@PathParam("processId") long processId )
    {

        try {
            List<Event> events = processBean.getEventsByProcessID( processId );

            List<FrequencyDTO> frequencies = activityBean.computeFrequencyEffortProcess( events );
            return Response.ok( frequencies ).build();

        }
        catch ( EntityDoesNotExistException ex)
        {

            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }

    }*/


    //RESOURCE HOURS FOR EACH ACTIVITY
    @ApiOperation( value = "Get total (users) work hours for each activity", response = ActivityTotalWorkHoursDTO.class, responseContainer = "List")
    @GET
    @UnitOfWork
    @Path( "{processId}/workhours" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getUsersWorkHours (@PathParam("processId") long processId) {
        try {

            List<Event> events = processBean.getEventsByProcessID( processId );

            List<ActivityTotalWorkHoursDTO> activitiesTotalWorkHours;

            if(processBean.findById(processId).getSubProcess() != null){ //processo de mould
                HashMap<String, List<Event>> eventsByMouldCode = processBean.sortEventsByMouldCode(events);

                activitiesTotalWorkHours = activityBean.getTotalWorkHoursActivities( eventsByMouldCode );
            }else{ //subprocesso de part
                HashMap<String, List<Event>> eventsByPartCode = processBean.sortEventsByPartCode(events);

                activitiesTotalWorkHours = activityBean.getTotalWorkHoursActivities( eventsByPartCode );
            }

            return Response.ok(activitiesTotalWorkHours).build();

        } catch (EntityDoesNotExistException e) {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( e.getMessage() ) .build();
        }


    }

    @ApiOperation( value = "Get total (users) work hours for each activity, through a filtered list of events", response = ActivityTotalWorkHoursDTO.class, responseContainer = "List")
    @POST
    @UnitOfWork
    @Path( "{processId}/workhours" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getUsersWorkHoursFilter (
            @PathParam("processId") long processId,
            @NotNull(message = "- filter is required.")
                    FilterInputWrapperDTO wrapper
    ) {
        try {
            FilterWrapper filter = new FilterWrapper( wrapper );

            List<ActivityTotalWorkHoursDTO> activitiesTotalWorkHours;

            if(processBean.findById(processId).getSubProcess() != null){ //processo de mould
                HashMap<String, List<Event>> eventsByMouldCode = processBean.getEventsByMouldCodeFromFilter(processId, filter);

                activitiesTotalWorkHours = activityBean.getTotalWorkHoursActivities( eventsByMouldCode );
            }else{ //subprocesso de part
                HashMap<String, List<Event>> eventsByPartCode = processBean.getEventsByPartCodeFromFilter(processId, filter);

                activitiesTotalWorkHours = activityBean.getTotalWorkHoursActivities( eventsByPartCode );
            }

            return Response.ok(activitiesTotalWorkHours).build();

        }
        // Eg. invalid dates format
        catch ( IllegalArgumentException ex )
        {
            return Response.status(Response.Status.NOT_ACCEPTABLE) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
        // No results for filter
        catch ( MyException e )
        {
            return Response.status( Response.Status.NO_CONTENT ).build();
        }
        catch (EntityDoesNotExistException e) {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( e.getMessage() ) .build();
        }

    }


    @ApiOperation( value = "Get total (workstations) operational hours for each activity", response = ActivityTotalOperationalHoursDTO.class, responseContainer = "List")
    @GET
    @UnitOfWork
    @Path( "{processId}/operationalhours" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getWorkstationsOperationalHours (@PathParam("processId") long processId) {
        try {

            List<Event> events = processBean.getEventsByProcessID( processId );
            List<ActivityTotalOperationalHoursDTO> activitiesTotalOperationalHours;

            if(processBean.findById(processId).getSubProcess() != null){ //processo de mould
                HashMap<String, List<Event>> eventsByMouldCode = processBean.sortEventsByMouldCode(events);

                activitiesTotalOperationalHours = activityBean.getTotalOperationalHoursActivity( eventsByMouldCode );
            }else{ //subprocesso de part
                HashMap<String, List<Event>> eventsByPartCode = processBean.sortEventsByPartCode(events);

                activitiesTotalOperationalHours = activityBean.getTotalOperationalHoursActivity( eventsByPartCode );
            }

            return Response.ok(activitiesTotalOperationalHours).build();

        }
        catch ( EntityDoesNotExistException ex )
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
    }


    @ApiOperation( value = "Get total (workstations) operational hours for each activity, through a filtered list of events ", response = ActivityTotalOperationalHoursDTO.class, responseContainer = "List")
    @POST
    @UnitOfWork
    @Path( "{processId}/operationalhours" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getWorkstationsOperationalHoursFilter (
            @PathParam("processId") long processId,
            @NotNull(message = "- filter is required.")
                    FilterInputWrapperDTO wrapper
    ) {
        try {
            FilterWrapper filter = new FilterWrapper( wrapper );

            List<ActivityTotalOperationalHoursDTO> activitiesTotalOperationalHours;

            if(processBean.findById(processId).getSubProcess() != null){ //processo de mould
                HashMap<String, List<Event>> eventsByMouldCode = processBean.getEventsByMouldCodeFromFilter(processId, filter);

                activitiesTotalOperationalHours = activityBean.getTotalOperationalHoursActivity( eventsByMouldCode );
            }else{ //subprocesso de part
                HashMap<String, List<Event>> eventsByPartCode = processBean.getEventsByPartCodeFromFilter(processId, filter);

                activitiesTotalOperationalHours = activityBean.getTotalOperationalHoursActivity( eventsByPartCode );
            }

            return Response.ok(activitiesTotalOperationalHours).build();

        }
        // Eg. invalid dates format
        catch ( IllegalArgumentException ex )
        {
            return Response.status(Response.Status.NOT_ACCEPTABLE) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
        // No results for filter
        catch ( MyException e )
        {
            return Response.status( Response.Status.NO_CONTENT ).build();
        }
        catch (EntityDoesNotExistException e) {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( e.getMessage() ) .build();
        }


    }

}