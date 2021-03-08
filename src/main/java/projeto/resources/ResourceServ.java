package projeto.resources;


import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.*;
import projeto.api.dtos.WorkstationPerformanceDTO;
import projeto.api.dtos.compare.FilterInputWrapperDTO;
import projeto.api.dtos.resources.ResourcesActivityDTO;
import projeto.api.dtos.resources.operationalhours.ActivityWorkstationsDTO;
import projeto.api.dtos.resources.workhours.ActivityUsersDTO;
import projeto.api.dtos.resources.workhours.WorkstationUsersDTO;
import projeto.auth.JwtBean;
import projeto.controller.ProcessBean;
import projeto.controller.ResourceBean;
import projeto.controller.WorkstationBean;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.MyException;
import projeto.core.Event;
import projeto.core.extra.FilterWrapper;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;

@SwaggerDefinition(tags = { @Tag(name = "resources", description = "Resources general statistics") })
@Api("resources")
@Path("resources")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ResourceServ
{
    private final ResourceBean resourceBean;
    private final JwtBean jwtBean;

    public ResourceServ(ResourceBean resourceBean, JwtBean jwtBean) {
        this.resourceBean = resourceBean;
        this.jwtBean = jwtBean;
    }

    /*@ApiOperation( value = "Get resources performance given a specific activity", response = ResourcesActivityDTO.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Log not found") })
    @GET
    @UnitOfWork
    @Path( "log/{logId}" )
    public Response getCasesPerformance (
            @PathParam("logId") long logId,
            @NotNull(message = "- activity name is required.")
            @QueryParam("activity") String activity ) {

        try {

            LogBean logBean = resourceBean.getLogBean();
            List<Event> events = logBean.getEventsByLogID( logId );
            HashMap<Long, List<Event>> eventsByCaseID = logBean.sortEventsByCaseID(events);

            ResourcesActivityDTO resourcePerformanceDTO = resourceBean.getResourcePerformance( activity, eventsByCaseID );

            return Response.ok( resourcePerformanceDTO ).build();

        }
        catch ( EntityDoesNotExistException ex )
        {

            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }

    }*/

    /*@ApiOperation( value = "Get events of Case IDs from a Log considering the model event nodes", response = ResourcesActivityDTO.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Log or cases not found"),
            @ApiResponse(code = 204, message = "No events found with requested filter") })
    @POST
    @UnitOfWork
    @Path( "log/{logId}" )
    public Response getCasesPerformance (
            @PathParam("logId") long logId,
            @NotNull(message = "- activity name is required.")
            @QueryParam("activity") String activity,
            @NotNull(message = "- filter is required.")
                    FilterInputWrapperDTO wrapper) {

        try {

            FilterWrapper filter = new FilterWrapper( wrapper );
            HashMap<Long, List<Event>> eventsByCaseID = resourceBean.getLogBean()
                    .getCasesFromFilter(logId, filter);

            ResourcesActivityDTO resourcePerformanceDTO = resourceBean.getResourcePerformance( activity, eventsByCaseID );

            return Response.ok( resourcePerformanceDTO ).build();

        }
        catch ( EntityDoesNotExistException ex )
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
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

    }*/


    @ApiOperation( value = "Get users average performance given a specific activity", response = ResourcesActivityDTO.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ") })
    @GET
    @UnitOfWork
    @Path( "{processId}/users/performance" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getUsersPerformanceActivity (
            @PathParam("processId") long processId,
            @NotNull(message = "- activity id is required.")
            @QueryParam("activity") long activityId,
            @ApiParam(value = "threshold value between 0 and 1 | used to consider which resources will be considered according to the time they spent performing the activity of an event")
            @QueryParam("threshold")
            @Min( value = 0, message = "- threshold must be between 0 and 1")
            @Max( value = 1, message = "- threshold must be between 0 and 1")
                    Float threshold
    ) {

        try {
            if( threshold == null ){
                threshold = 0.8F;
            }

            ProcessBean processBean = resourceBean.getProcessBean();
            List<Event> events = processBean.getEventsByProcessID( processId );

            ResourcesActivityDTO resourcePerformanceDTO;

            if(processBean.findById(processId).getSubProcess() != null){ //processo de mould
                HashMap<String, List<Event>> eventsByMouldCode = processBean.sortEventsByMouldCode(events);

                resourcePerformanceDTO = resourceBean.getUsersPerformance( activityId, eventsByMouldCode, threshold, false );
            }else{ //subprocesso de part
                HashMap<String, List<Event>> eventsByPartCode = processBean.sortEventsByPartCode(events);

                resourcePerformanceDTO = resourceBean.getUsersPerformance( activityId, eventsByPartCode, threshold, true );
            }
            return Response.ok( resourcePerformanceDTO ).build();
        }
        catch ( EntityDoesNotExistException ex )
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
    }

    @ApiOperation( value = "Get users average performance given a specific activity, through a filtered list of events", response = ResourcesActivityDTO.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ") })
    @POST
    @UnitOfWork
    @Path( "{processId}/users/performance" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getUsersPerformanceActivityFilter (
            @PathParam("processId") long processId,
            @NotNull(message = "- activity id is required.")
            @QueryParam("activity") long activityId,
            @ApiParam(value = "threshold value between 0 and 1 | used to consider which resources will be considered according to the time they spent performing the activity of an event")
            @QueryParam("threshold")
            @Min( value = 0, message = "- threshold must be between 0 and 1")
            @Max( value = 1, message = "- threshold must be between 0 and 1")
                    Float threshold,
            @NotNull(message = "- filter is required.")
                    FilterInputWrapperDTO wrapper) {

        try {
            if( threshold == null ){
                threshold = 0.8F;
            }

            FilterWrapper filter = new FilterWrapper( wrapper );

            ProcessBean processBean = resourceBean.getProcessBean();
            ResourcesActivityDTO resourcePerformanceDTO;

            if(processBean.findById(processId).getSubProcess() != null){ //processo de mould
                HashMap<String, List<Event>> eventsByMouldCode = processBean.getEventsByMouldCodeFromFilter(processId, filter);

                resourcePerformanceDTO = resourceBean.getUsersPerformance(activityId, eventsByMouldCode, threshold, false );
            }else{ //subprocesso de part
                HashMap<String, List<Event>> eventsByPartCode = processBean.getEventsByPartCodeFromFilter(processId, filter);

                resourcePerformanceDTO = resourceBean.getUsersPerformance(activityId, eventsByPartCode, threshold, true );
            }

            return Response.ok( resourcePerformanceDTO ).build();

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


    //SINGLE USER
    @ApiOperation( value = "Get specific user average performance given a specific activity", response = ResourcesActivityDTO.class)
    @GET
    @UnitOfWork
    @Path( "{processId}/users/{username}/performance" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getSingleUserPerformanceActivity (
            @Auth @HeaderParam("Authorization") String auth,
            @PathParam("processId") long processId,
            @NotNull(message = "- username is required.")
            @PathParam("username") String username,
            @NotNull(message = "- activity id is required.")
            @QueryParam("activity") long activityId,
            @ApiParam(value = "threshold value between 0 and 1 | used to consider which resources will be considered according to the time they spent performing the activity of an event")
            @QueryParam("threshold")
            @Min( value = 0, message = "- threshold must be between 0 and 1")
            @Max( value = 1, message = "- threshold must be between 0 and 1")
                    Float threshold
    ) {

        try {
            if(resourceBean.getUserBean().findByUsername(username) == null){
                throw new EntityDoesNotExistException("Utilizador com o username " + username + " não encontrado");
            }

            if (auth != null && auth.startsWith("Bearer ")) {
                String jwt = auth.substring(7);
                Claims claims = jwtBean.decodeJWT(jwt);
                if(!claims.getSubject().equals(username)){
                    return Response.status(Response.Status.UNAUTHORIZED).type( MediaType.TEXT_PLAIN ).entity("Não pode aceder a informação sobre outros utilizadores").build();
                }
            }

            if( threshold == null ){
                threshold = 0.8F;
            }

            ProcessBean processBean = resourceBean.getProcessBean();
            List<Event> events = processBean.getEventsByProcessID( processId );

            ResourcesActivityDTO resourcePerformanceDTO;

            if(processBean.findById(processId).getSubProcess() != null){ //processo de mould
                HashMap<String, List<Event>> eventsByMouldCode = processBean.sortEventsByMouldCode(events);

                //resourcePerformanceDTO = resourceBean.getResourcePerformanceProcess( activityId, eventsByMouldCode, threshold, false );
                resourcePerformanceDTO = resourceBean.getSingleUserPerformance( activityId, username, eventsByMouldCode, threshold, false );
            }else{ //subprocesso de part
                HashMap<String, List<Event>> eventsByPartCode = processBean.sortEventsByPartCode(events);

                resourcePerformanceDTO = resourceBean.getSingleUserPerformance( activityId, username, eventsByPartCode, threshold, true );
            }

            return Response.ok( resourcePerformanceDTO ).build();

        }
        catch ( EntityDoesNotExistException ex )
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN ).entity( ex.getMessage() ).build();
        }

    }


    @ApiOperation( value = "Get specific user average performance given a specific activity, through a filtered list of events", response = ResourcesActivityDTO.class)
    @POST
    @UnitOfWork
    @Path( "{processId}/users/{username}/performance" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getSingleUsersPerformanceActivityFilter (
            @Auth @HeaderParam("Authorization") String auth,
            @PathParam("processId") long processId,
            @NotNull(message = "- username is required.")
            @PathParam("username") String username,
            @NotNull(message = "- activity id is required.")
            @QueryParam("activity") long activityId,
            @ApiParam(value = "threshold value between 0 and 1 | used to consider which resources will be considered according to the time they spent performing the activity of an event")
            @QueryParam("threshold")
            @Min( value = 0, message = "- threshold must be between 0 and 1")
            @Max( value = 1, message = "- threshold must be between 0 and 1")
                    Float threshold,
            @NotNull(message = "- filter is required.")
                    FilterInputWrapperDTO wrapper) {

        try {
            if(resourceBean.getUserBean().findByUsername(username) == null){
                throw new EntityDoesNotExistException("Utilizador com o username " + username + " não encontrado");
            }

            if (auth != null && auth.startsWith("Bearer ")) {
                String jwt = auth.substring(7);
                Claims claims = jwtBean.decodeJWT(jwt);
                if(!claims.getSubject().equals(username)){
                    return Response.status(Response.Status.UNAUTHORIZED).type( MediaType.TEXT_PLAIN ).entity("Não pode aceder a informação sobre outros utilizadores").build();
                }
            }

            if( threshold == null ){
                threshold = 0.8F;
            }

            FilterWrapper filter = new FilterWrapper( wrapper );

            ProcessBean processBean = resourceBean.getProcessBean();
            ResourcesActivityDTO resourcePerformanceDTO;

            if(processBean.findById(processId).getSubProcess() != null){ //processo de mould
                HashMap<String, List<Event>> eventsByMouldCode = processBean.getEventsByMouldCodeFromFilter(processId, filter);

                resourcePerformanceDTO = resourceBean.getSingleUserPerformance(activityId, username, eventsByMouldCode, threshold, false );
            }else{ //subprocesso de part
                HashMap<String, List<Event>> eventsByPartCode = processBean.getEventsByPartCodeFromFilter(processId, filter);

                resourcePerformanceDTO = resourceBean.getSingleUserPerformance(activityId, username, eventsByPartCode, threshold, true );
            }

            return Response.ok( resourcePerformanceDTO ).build();

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
        catch ( EntityDoesNotExistException ex )
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
    }


    //NEW STATISTICS - Users work hours in specific activity

    //USERS - WORK HOURS
    @ApiOperation( value = "Get total users work hours in a specific activity", response = ActivityUsersDTO.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ") })
    @GET
    @UnitOfWork
    @Path( "{processId}/users/workhours/activities" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getUsersWorkHoursActivity (
            @PathParam("processId") long processId,
            @NotNull(message = "- activity id is required.")
            @QueryParam("activity") long activityId
    ) {
        try {

            ProcessBean processBean = resourceBean.getProcessBean();
            List<Event> events = processBean.getEventsByProcessID( processId );

            ActivityUsersDTO activityUsersDTO;

            if(processBean.findById(processId).getSubProcess() != null){ //processo de mould
                HashMap<String, List<Event>> eventsByMouldCode = processBean.sortEventsByMouldCode(events);

                activityUsersDTO = resourceBean.getUsersWorkHoursActivity( activityId, eventsByMouldCode, false );
            }else{ //subprocesso de part
                HashMap<String, List<Event>> eventsByPartCode = processBean.sortEventsByPartCode(events);

                activityUsersDTO = resourceBean.getUsersWorkHoursActivity( activityId, eventsByPartCode, true );
            }

            return Response.ok(activityUsersDTO).build();

        }
        catch ( EntityDoesNotExistException ex )
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
    }

    @ApiOperation( value = "Get total users work hours given a specific activity, through a filtered list of events", response = ActivityUsersDTO.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ") })
    @POST
    @UnitOfWork
    @Path( "{processId}/users/workhours/activities" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getUsersWorkHoursActivityFilter (
            @PathParam("processId") long processId,
            @NotNull(message = "- activity id is required.")
            @QueryParam("activity") long activityId,
            @NotNull(message = "- filter is required.")
                    FilterInputWrapperDTO wrapper
    ) {
        try {
            FilterWrapper filter = new FilterWrapper( wrapper );

            ProcessBean processBean = resourceBean.getProcessBean();
            ActivityUsersDTO activityUsersDTO;

            if(processBean.findById(processId).getSubProcess() != null){ //processo de mould
                HashMap<String, List<Event>> eventsByMouldCode = processBean.getEventsByMouldCodeFromFilter(processId, filter);

                activityUsersDTO = resourceBean.getUsersWorkHoursActivity( activityId, eventsByMouldCode, false );
            }else{ //subprocesso de part
                HashMap<String, List<Event>> eventsByPartCode = processBean.getEventsByPartCodeFromFilter(processId, filter);

                activityUsersDTO = resourceBean.getUsersWorkHoursActivity( activityId, eventsByPartCode, true );
            }

            return Response.ok(activityUsersDTO).build();

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
        catch ( EntityDoesNotExistException ex )
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }

    }


    @ApiOperation( value = "Get total users work hours in a specific workstation", response = WorkstationUsersDTO.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ") })
    @GET
    @UnitOfWork
    @Path( "{processId}/users/workhours/workstations" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getUsersWorkHoursWorkstation (
            @PathParam("processId") long processId,
            @NotNull(message = "- workstation id is required.")
            @QueryParam("workstation") long workstationId
    ) {

        try {

            ProcessBean processBean = resourceBean.getProcessBean();
            List<Event> events = processBean.getEventsByProcessID( processId );

            WorkstationUsersDTO workstationUsersDTO;

            if(processBean.findById(processId).getSubProcess() != null){ //processo de mould
                HashMap<String, List<Event>> eventsByMouldCode = processBean.sortEventsByMouldCode(events);

                workstationUsersDTO = resourceBean.getUsersWorkHoursWorkstation( workstationId, eventsByMouldCode, false );
            }else{ //subprocesso de part
                HashMap<String, List<Event>> eventsByPartCode = processBean.sortEventsByPartCode(events);

                workstationUsersDTO = resourceBean.getUsersWorkHoursWorkstation( workstationId, eventsByPartCode, true );
            }

            return Response.ok(workstationUsersDTO).build();

        }
        catch ( EntityDoesNotExistException ex )
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }

    }


    @ApiOperation( value = "Get total users work hours in a specific workstation, through a filtered list of events", response = WorkstationUsersDTO.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ") })
    @POST
    @UnitOfWork
    @Path( "{processId}/users/workhours/workstations" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getUsersWorkHoursWorkstationFilter (
            @PathParam("processId") long processId,
            @NotNull(message = "- workstation id is required.")
            @QueryParam("workstation") long workstationId,
            @NotNull(message = "- filter is required.")
                    FilterInputWrapperDTO wrapper
    ) {
        try {
            FilterWrapper filter = new FilterWrapper( wrapper );

            ProcessBean processBean = resourceBean.getProcessBean();
            WorkstationUsersDTO workstationUsersDTO;

            if(processBean.findById(processId).getSubProcess() != null){ //processo de mould
                HashMap<String, List<Event>> eventsByMouldCode = processBean.getEventsByMouldCodeFromFilter(processId, filter);

                workstationUsersDTO = resourceBean.getUsersWorkHoursWorkstation( workstationId, eventsByMouldCode, false );
            }else{ //subprocesso de part
                HashMap<String, List<Event>> eventsByPartCode = processBean.getEventsByPartCodeFromFilter(processId, filter);

                workstationUsersDTO = resourceBean.getUsersWorkHoursWorkstation( workstationId, eventsByPartCode, true );
            }

            return Response.ok(workstationUsersDTO).build();

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
        catch ( EntityDoesNotExistException ex )
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }

    }




    //WORKSTATIONS - OPERATIONAL HOURS
    @ApiOperation( value = "Get workstations average duration in a specific activity", response = WorkstationPerformanceDTO.class, responseContainer = "List")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ") })
    @GET
    @UnitOfWork
    @Path("{processId}/workstations/performance")
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getWorkstationsPerformanceActivity(@PathParam("processId") long processId,
                                         @NotNull(message = "- activity id is required.")
                                         @QueryParam("activity") long activityId
                                         )
    {

        try {
            List<Event> events = resourceBean.getProcessBean().getEventsByProcessID( processId );

            List<WorkstationPerformanceDTO> performances = resourceBean.getWorkstationBean().computeWorkstationsPerformance( events, activityId );
            return Response.ok( performances ).build();

        }
        catch ( EntityDoesNotExistException ex)
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }

    }



    @ApiOperation( value = "Get workstations total operational hours in a specific activity", response = ActivityWorkstationsDTO.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ") })
    @GET
    @UnitOfWork
    @Path( "{processId}/workstations/operationalhours" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getWorkstationsOperationalHoursActivity (
            @PathParam("processId") long processId,
            @NotNull(message = "- activity id is required.")
            @QueryParam("activity") long activityId
    ) {

        try {

            ProcessBean processBean = resourceBean.getProcessBean();
            List<Event> events = processBean.getEventsByProcessID( processId );

            ActivityWorkstationsDTO activityWorkstationsDTO;

            if(processBean.findById(processId).getSubProcess() != null){ //processo de mould
                HashMap<String, List<Event>> eventsByMouldCode = processBean.sortEventsByMouldCode(events);

                activityWorkstationsDTO = resourceBean.getWorkstationsOperationalHoursActivity( activityId, eventsByMouldCode, false );
            }else{ //subprocesso de part
                HashMap<String, List<Event>> eventsByPartCode = processBean.sortEventsByPartCode(events);

                activityWorkstationsDTO = resourceBean.getWorkstationsOperationalHoursActivity( activityId, eventsByPartCode, true );
            }

            return Response.ok(activityWorkstationsDTO).build();
        }
        catch ( EntityDoesNotExistException ex )
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }

    }


    @ApiOperation( value = "Get workstations total operational hours in a specific activity, through a filtered list of events", response = ActivityWorkstationsDTO.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ") })
    @POST
    @UnitOfWork
    @Path( "{processId}/workstations/operationalhours" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getWorkstationsOperationalHoursActivityFilter (
            @PathParam("processId") long processId,
            @NotNull(message = "- activity id is required.")
            @QueryParam("activity") long activityId,
            @NotNull(message = "- filter is required.")
                    FilterInputWrapperDTO wrapper
    ) {
        try {
            FilterWrapper filter = new FilterWrapper( wrapper );

            ProcessBean processBean = resourceBean.getProcessBean();
            ActivityWorkstationsDTO activityWorkstationsDTO;

            if(processBean.findById(processId).getSubProcess() != null){ //processo de mould
                HashMap<String, List<Event>> eventsByMouldCode = processBean.getEventsByMouldCodeFromFilter(processId, filter);

                activityWorkstationsDTO = resourceBean.getWorkstationsOperationalHoursActivity( activityId, eventsByMouldCode, false );
            }else{ //subprocesso de part
                HashMap<String, List<Event>> eventsByPartCode = processBean.getEventsByPartCodeFromFilter(processId, filter);

                activityWorkstationsDTO = resourceBean.getWorkstationsOperationalHoursActivity( activityId, eventsByPartCode, true );
            }

            return Response.ok(activityWorkstationsDTO).build();

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
        catch ( EntityDoesNotExistException ex )
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }

    }
    
}

