package projeto.resources;


import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import projeto.algorithms_process_mining.ProcessMiningAlgorithm;
import projeto.algorithms_process_mining.alpha_algorithm.AlphaAlgorithm;
import projeto.algorithms_process_mining.heuristic_miner.HeuristicMiner;
import projeto.algorithms_process_mining.inductive_miner.InductiveMiner;
import projeto.api.dtos.workflow_network.WorkflowNetworkDTO;
import projeto.controller.LogBean;
import projeto.controller.ProcessBean;
import projeto.controller.exceptions.EntityDoesNotExistException;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@SwaggerDefinition(tags = { @Tag(name = "workflow-network", description = "Workflow network and statistics") })
@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ") })
@Api("workflow-network")
@Path("workflow-network")
public class WorkflowNetworkServ
{

    private final LogBean logBean;
    private final ProcessBean processBean;

    public WorkflowNetworkServ(LogBean logBean, ProcessBean processBean) {
        this.logBean = logBean;
        this.processBean = processBean;
    }

    /*@ApiOperation( value = "Get the workflow network with Alpha Miner of a log", response = WorkflowNetworkDTO.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Log not found") })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path( "alpha-miner/{id}" )
    public Response getWorkflowNetworkAlphaAlgorithm( @PathParam("id") long logID )
    {
        AlphaAlgorithm algorithm = new AlphaAlgorithm();
        return getWorkflowNetworkByLogID( logID, algorithm );

    }

    @ApiOperation( value = "Get the workflow network with Heuristic Miner of a log", response = WorkflowNetworkDTO.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Log not found") })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path( "heuristic-miner/{id}" )
    public Response getWorkflowNetworkHeuristicMiner(
            @PathParam("id") long logID,
            @ApiParam(value = "threshold value between 0 and 1 | used to consider relations in the footprint if  the heuristic value >= threshold")
            @QueryParam("threshold")
            @Min( value = 0, message = "- threshold must be between 0 and 1")
            @Max( value = 1, message = "- threshold must be between 0 and 1")
            Float threshold )
    {
        if( threshold == null )
            threshold = 0.8F;

        HeuristicMiner algorithm = new HeuristicMiner( threshold );
        return getWorkflowNetworkByLogID( logID, algorithm );

    }


    private Response getWorkflowNetworkByLogID( long logID, ProcessMiningAlgorithm algorithm )
    {

        try {

            WorkflowNetworkDTO workflowNetwork = logBean.getWorkFlowNetworkFromLog( logID, algorithm );

            return Response.ok( workflowNetwork ).build();

        }
        catch ( EntityDoesNotExistException ex)
        {

            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
            //return null;
        }

    }*/

    @ApiOperation( value = "Get the workflow network with Alpha Miner of a process", response = WorkflowNetworkDTO.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Process not found") })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path( "alpha-miner/processes/{processId}" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getWorkflowNetworkAlphaAlgorithmProcess( @PathParam("processId") long processId )
    {
        AlphaAlgorithm algorithm = new AlphaAlgorithm();
        return getWorkflowNetworkByProcessId( processId, algorithm );

    }

    @ApiOperation( value = "Get the workflow network with Heuristic Miner of a process", response = WorkflowNetworkDTO.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Process not found") })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path( "heuristic-miner/processes/{processId}" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getWorkflowNetworkHeuristicMinerProcess(
            @PathParam("processId") long processId,
            @ApiParam(value = "threshold value between 0 and 1 | used to consider relations in the footprint if  the heuristic value >= threshold")
            @QueryParam("threshold")
            @Min( value = 0, message = "- threshold must be between 0 and 1")
            @Max( value = 1, message = "- threshold must be between 0 and 1")
                    Float threshold )
    {
        if( threshold == null )
            threshold = 0.8F;

        HeuristicMiner algorithm = new HeuristicMiner( threshold );
        return getWorkflowNetworkByProcessId( processId, algorithm );

    }

    @ApiOperation( value = "Get the workflow network with Inductive Miner of a process", response = WorkflowNetworkDTO.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Process not found") })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path( "inductive-miner/processes/{processId}" )
    //@RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getWorkflowNetworkInductiveMinerProcess(
            @PathParam("processId") long processId,
            @ApiParam(value = "threshold value between 0 and 1 | used to consider relations in the footprint if  the heuristic value >= threshold")
            @QueryParam("threshold")
            @Min( value = 0, message = "- threshold must be between 0 and 1")
            @Max( value = 1, message = "- threshold must be between 0 and 1")
                    Float threshold )
    {
        if( threshold == null )
            threshold = 0.8F;

        InductiveMiner algorithm = new InductiveMiner(threshold);
        return getWorkflowNetworkByProcessId( processId, algorithm );

    }


    private Response getWorkflowNetworkByProcessId( long processId, ProcessMiningAlgorithm algorithm )
    {
        try {
            WorkflowNetworkDTO workflowNetwork;
            if (algorithm.getClass() == InductiveMiner.class){
                workflowNetwork = processBean.getWorkFlowNetworkFromProcess( processId, (InductiveMiner) algorithm );
            }
            else{
                workflowNetwork = processBean.getWorkFlowNetworkFromProcess( processId, algorithm );
            }
            return Response.ok( workflowNetwork ).build();
        }
        catch ( EntityDoesNotExistException ex)
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }

    }
}

