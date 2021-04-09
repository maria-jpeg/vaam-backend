package projeto.resources;


import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import org.processmining.plugins.directlyfollowsgraph.DirectlyFollowsGraph;
import org.processmining.plugins.inductiveVisualMiner.plugins.GraphvizProcessTree;
import projeto.algorithms_process_mining.ProcessMiningAlgorithm;
import projeto.algorithms_process_mining.alpha_algorithm.AlphaAlgorithm;
import projeto.algorithms_process_mining.heuristic_miner.HeuristicMiner;
import projeto.api.dtos.conformance.CaseConformanceInputWrapperDTO;
import projeto.api.dtos.conformance.CasePerformanceDTO;
import projeto.api.dtos.conformance.FiltersDTO;
import projeto.api.dtos.conformance.deviations.ConformanceNetworkDeviationsDTO;
import projeto.api.dtos.conformance.performance.ConformanceNetworkPerformanceDTO;
import projeto.api.dtos.inductiveminer.DotDTO;
import projeto.controller.ConformanceBean;
import projeto.controller.EventBean;
import projeto.controller.ProcessBean;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.MyException;
import projeto.core.Activity;
import projeto.core.ActivityUserEntry;
import projeto.core.Event;
import projeto.core.User;
import projeto.core.extra.FilterWrapper;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@SwaggerDefinition(tags = { @Tag(name = "conformance", description = "Conformance of activities") })
@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ") })
@Api("conformance")
@Path("conformance")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConformanceServ
{
    private final ConformanceBean conformanceBean;

    //im testing
    private final EventBean eventBean;

    public ConformanceServ(ConformanceBean conformanceBean, EventBean eventBean) { this.conformanceBean = conformanceBean; this.eventBean = eventBean; }

    /*@ApiOperation( value = "Conformance of the Performance with Alpha Miner of a log giving other log", response = ConformanceNetworkPerformanceDTO.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Log or Log model not found") })
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path( "performance/alpha-miner/{id}/with/{modelid}" )
    public Response getConformancePerformanceAlphaMiner(
            @PathParam("id") long logID,
            @PathParam("modelid") long modelLogID )
    {
        AlphaAlgorithm algorithm = new AlphaAlgorithm();
        return getConformancePerformanceModel( logID, modelLogID, algorithm );

    }

    @ApiOperation( value = "Conformance of the Performance with Heuristic Miner of a log giving other log", response = ConformanceNetworkPerformanceDTO.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Log or Log model not found") })
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path( "performance/heuristic-miner/{id}/with/{modelid}" )
    public Response getConformancePerformanceHeuristicMiner(
            @PathParam("id") long logID,
            @PathParam("modelid") long modelLogID,
            @ApiParam(value = "threshold value between 0 and 1 | used to consider relations in the footprint if  the heuristic value >= threshold")
            @QueryParam("threshold")
            @Min( value = 0, message = "- threshold must be between 0 and 1")
            @Max( value = 1, message = "- threshold must be between 0 and 1")
            Float threshold )
    {
        if( threshold == null )
            threshold = 0.8F;

        HeuristicMiner algorithm = new HeuristicMiner( threshold );
        return getConformancePerformanceModel( logID, modelLogID, algorithm );

    }

    private Response getConformancePerformanceModel( long logID, long modelLogID, ProcessMiningAlgorithm algorithm )
    {
        try {

            ConformanceNetworkPerformanceDTO notConformancesNetwork = conformanceBean.getNetworkModelPerformance(logID, modelLogID, algorithm);
            return Response.ok( notConformancesNetwork ).build();

        }
        catch ( EntityDoesNotExistException ex)
        {

            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }

    }*/



    /*@ApiOperation( value = "Conformance of the Deviations of a log giving other log", response = ConformanceNetworkDeviationsDTO.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Log or Log model not found") })
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path( "deviations/{id}/with/{modelid}" )
    public Response getConformanceDeviations(
            @PathParam("id") long logId,
            @PathParam("modelid") long modelLogId)
    {

        try {

            ConformanceNetworkDeviationsDTO notConformancesNetworkDeviations = conformanceBean.getNotConformancesNetworkDeviations(logId, modelLogId);

            return Response.ok( notConformancesNetworkDeviations ).build();

        }
        catch ( EntityDoesNotExistException ex)
        {

            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
            //return null;
        }

    }*/



    /*@ApiOperation( value = "Get events of Case IDs from a Log considering the model event nodes", response = CasePerformanceDTO.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Log or cases not found"),
            @ApiResponse(code = 204, message = "No events found with requested filter") })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path( "log/{logId}" )
    public Response getEventsPerformance(
            @PathParam("logId") long logId,
            @NotNull(message = "- filter required.")
                    CaseConformanceInputWrapperDTO wrapper) {

        try {

            List<String> eventNames = wrapper.getNodes();


            // Check if cases are present
            //if( cases.isEmpty() )
                //return Response.status(Response.Status.NOT_ACCEPTABLE) .type( MediaType.TEXT_PLAIN )
                        //.entity( "cases are required" ) .build();


            FilterWrapper filter = new FilterWrapper( wrapper );

            // Check if eventNames are present
            if( eventNames.isEmpty() )
                return Response.status(Response.Status.NOT_ACCEPTABLE) .type( MediaType.TEXT_PLAIN )
                        .entity( "nodes are required" ) .build();

            // Check is que nodes are unique
            Set<String> uniqueEventNames = new LinkedHashSet<>(); // LinkedHashSet
            for (String e : eventNames)
            {
                //System.out.println( e );

                if( ! uniqueEventNames.add( e ) )
                    return Response.status(Response.Status.NOT_ACCEPTABLE) .type( MediaType.TEXT_PLAIN )
                            .entity( "nodes elements cant be duplicated! each node must be unique!" ) .build();

                //System.out.println( "=================================================================================" );
            }


            HashMap<Long, List<Event>> caseEvents = conformanceBean.getLogBean()
                    .getCasesFromFilter(logId, filter);

            CasePerformanceDTO eventsByCaseIDfromLog = conformanceBean.getEventsPerformance( caseEvents, uniqueEventNames );

            return Response.ok( eventsByCaseIDfromLog ).build();

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


    //COMPARISSON BETWEEN 2 PROCESSES

    //CONFORMANCE SENDING A SINGLE PROCESS (DATA RANGE OR MOULD/PART RANGE)

    @ApiOperation( value = "Conformance of the deviations of a process comparing with a model process", response = ConformanceNetworkDeviationsDTO.class)
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path( "deviations/{processId}/with/{modelProcessId}" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getConformanceDeviations(
            @PathParam("processId") long processId,
            @PathParam("modelProcessId") long modelProcessId)
    {

        try {

            ConformanceNetworkDeviationsDTO notConformancesNetworkDeviations = conformanceBean.getNotConformancesNetworkDeviationsProcess(processId, modelProcessId);

            return Response.ok( notConformancesNetworkDeviations ).build();

        }
        catch ( EntityDoesNotExistException ex)
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
        catch (MyException ex) {
            return Response.status(Response.Status.CONFLICT) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }

    }


    @ApiOperation( value = "Conformance of the performance with Alpha Miner of a process", response = ConformanceNetworkPerformanceDTO.class)
    @POST
    @UnitOfWork
    @Path( "performance/alpha-miner/model/{modelProcessId}/" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getConformancePerformanceAlphaMinerProcess(
            @PathParam("modelProcessId") long modelProcessId,
            @NotNull(message = "- filter required.")
                    CaseConformanceInputWrapperDTO wrapper)
    {
        AlphaAlgorithm algorithm = new AlphaAlgorithm();
        return getConformancePerformanceModelProcess( modelProcessId, wrapper, algorithm );

    }


    @ApiOperation( value = "Performance of a model process with Heuristic Miner", response = ConformanceNetworkPerformanceDTO.class)
    @POST
    @UnitOfWork
    @Path( "performance/heuristic-miner/model/{modelProcessId}" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getConformancePerformanceHeuristicMinerProcess(
            @PathParam("modelProcessId") long modelProcessId,
            @ApiParam(value = "threshold value between 0 and 1 | used to consider relations in the footprint if  the heuristic value >= threshold")
            @QueryParam("threshold")
            @Min( value = 0, message = "- threshold must be between 0 and 1")
            @Max( value = 1, message = "- threshold must be between 0 and 1")
                    Float threshold,
            @NotNull(message = "- filter required.")
                    CaseConformanceInputWrapperDTO wrapper)
    {
        if( threshold == null )
            threshold = 0.8F;

        HeuristicMiner algorithm = new HeuristicMiner( threshold );
        return getConformancePerformanceModelProcess( modelProcessId, wrapper, algorithm );

    }


    private Response getConformancePerformanceModelProcess( long processId, CaseConformanceInputWrapperDTO wrapper, ProcessMiningAlgorithm algorithm )
    {
        try {
            FilterWrapper filter = new FilterWrapper( wrapper );

            if(conformanceBean.getProcessBean().findById(processId).getSubProcess() != null){ //é processo de molde
                HashMap<String, List<Event>> mouldEvents = conformanceBean.getProcessBean().getEventsByMouldCodeFromFilter(processId, filter); //events of moulds (instances) by the filters

                ConformanceNetworkPerformanceDTO notConformancesNetwork = conformanceBean.getNetworkModelPerformanceProcess(processId, mouldEvents, algorithm);

                return Response.ok( notConformancesNetwork ).build();

            }else{ //subprocesso
                HashMap<String, List<Event>> partEvents = conformanceBean.getProcessBean().getEventsByPartCodeFromFilter(processId, filter);

                ConformanceNetworkPerformanceDTO notConformancesNetwork = conformanceBean.getNetworkModelPerformanceProcess(processId, partEvents, algorithm);

                return Response.ok( notConformancesNetwork ).build();
            }


        }
        // Eg. invalid dates format
        catch ( IllegalArgumentException ex )
        {
            return Response.status(Response.Status.NOT_ACCEPTABLE) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
        catch ( EntityDoesNotExistException ex)
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        } catch (MyException e) {
            return Response.status(Response.Status.NO_CONTENT) .type( MediaType.TEXT_PLAIN )
                    .entity( e.getMessage() ) .build();
        }

    }


    @ApiOperation( value = "Performance of a process considering the model process nodes/activities", response = CasePerformanceDTO.class)
    @POST
    @UnitOfWork
    @Path( "performance/process/{processId}" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getEventsPerformance(
            @PathParam("processId") long processId,
            @NotNull(message = "- filter required.")
                    CaseConformanceInputWrapperDTO wrapper){
        try {

            List<String> eventNames = wrapper.getNodes();

            FilterWrapper filter = new FilterWrapper( wrapper );

            // Check if eventNames are present
            if( eventNames == null || eventNames.isEmpty() )
                return Response.status(Response.Status.NOT_ACCEPTABLE) .type( MediaType.TEXT_PLAIN )
                        .entity( "Nós (nodes) do processo modelo são obrigatórios" ) .build();

            // Check if nodes are unique
            LinkedHashSet<String> uniqueEventNames = new LinkedHashSet<>(); // LinkedHashSet
            for (String e : eventNames)
            {
                //System.out.println( e );

                if( ! uniqueEventNames.add( e ) )
                    return Response.status(Response.Status.NOT_ACCEPTABLE) .type( MediaType.TEXT_PLAIN )
                            .entity( "nodes elements cant be duplicated! each node must be unique!" ) .build();
            }

            if(conformanceBean.getProcessBean().findById(processId).getSubProcess() != null){ //é processo de molde
                HashMap<String, List<Event>> mouldEvents = conformanceBean.getProcessBean().getEventsByMouldCodeFromFilter(processId, filter); //events of moulds (instances) by the filters

                CasePerformanceDTO eventsByMouldCodefromProcess = conformanceBean.getEventsByMouldPerformance( mouldEvents, uniqueEventNames );

                return Response.ok( eventsByMouldCodefromProcess ).build();
            }else{ //subprocesso
                HashMap<String, List<Event>> partEvents = conformanceBean.getProcessBean().getEventsByPartCodeFromFilter(processId, filter);

                CasePerformanceDTO eventsByPartCodefromProcess = conformanceBean.getEventsByPartPerformance( partEvents, uniqueEventNames );

                return Response.ok( eventsByPartCodefromProcess ).build();
            }


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
            return Response.status(Response.Status.NO_CONTENT).type( MediaType.TEXT_PLAIN )
                    .entity( e.getMessage() ).build();
        }
        catch ( EntityDoesNotExistException ex )
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
    }


    @ApiOperation( value = "Get the activities, resources and moulds/parts (filter information) of a process", response = FiltersDTO.class)
    @GET
    @UnitOfWork
    @Path("{processId}/filterInformation")
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getActivitiesResourcesMouldsByProcessID( @PathParam("processId") long processId )
    {
        try {
            ProcessBean processBean = conformanceBean.getProcessBean();

            //activities
            List<Activity> activities = processBean.getActivitiesfromProcessId( processId );
            List<String> activityNames = new LinkedList<>();
            for (Activity activity : activities) {
                activityNames.add(activity.getName());
            }

            //resources
            List<ActivityUserEntry> entries = processBean.getUsersFromProcessId( processId );
            List<User> resources = new LinkedList<>();
            for (ActivityUserEntry e : entries) {
                if(!resources.contains(e.getUser())){
                    resources.add(e.getUser());
                }
            }
            List<String> resourceUsernames = new LinkedList<>();
            for (User resource : resources) {
                resourceUsernames.add(resource.getUsername());
            }

            FiltersDTO filtersDTO = null;
            //moulds/parts
            if(processBean.findById(processId).getSubProcess() != null){ //é processo de molde
                List<String> moulds = processBean.getMouldCodesfromProcessId(processId);
                filtersDTO = new FiltersDTO(activityNames,resourceUsernames,moulds,false);
            }else{ //subprocesso
                List<String> parts = processBean.getPartCodesfromProcessId(processId);
                filtersDTO = new FiltersDTO(activityNames,resourceUsernames,parts,true);
            }

            return Response.status(Response.Status.OK)
                    .entity(filtersDTO)
                    .build();

        }
        catch( EntityDoesNotExistException ex )
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
    }

    @ApiOperation( value = "Endpoint for testing")
    @GET
    @UnitOfWork
    @Path("/InductiveMinerTest")
    public Response getProcessTree( )
    {
        try{
            DotDTO dotDTO = eventBean.getEventTree();
            DirectlyFollowsGraph graph = eventBean.getDFG();
            //return Response.status(Response.Status.OK).entity(dotDTO).build();
            return Response.status(Response.Status.OK).entity(graph).build();

        }catch (GraphvizProcessTree.NotYetImplementedException e)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}

