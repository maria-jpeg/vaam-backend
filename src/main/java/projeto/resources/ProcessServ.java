package projeto.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import io.swagger.annotations.Tag;
import projeto.api.dtos.conformance.CasePerformanceDTO;
import projeto.api.dtos.conformance.FiltersDTO;
import projeto.api.dtos.entities.ProcessDTO;
import projeto.controller.ActivityBean;
import projeto.controller.ProcessBean;
import projeto.controller.UserBean;
import projeto.controller.WorkstationBean;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.EntityExistsException;
import projeto.controller.exceptions.MyException;
import projeto.core.*;
import projeto.core.Process;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@SwaggerDefinition(tags = { @Tag(name = "processes", description = "Operations related to Processes") })
@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ") })
@Api(value="processes")
@Path("/processes") // relative url web path of this controller
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class ProcessServ {

    private final ProcessBean processBean;
    private final ActivityBean activityBean;
    private final UserBean userBean;
    private final WorkstationBean workstationBean;

    public ProcessServ(ProcessBean processBean, ActivityBean activityBean, UserBean userBean, WorkstationBean workstationBean) {
        this.processBean = processBean;
        this.activityBean = activityBean;
        this.userBean = userBean;
        this.workstationBean = workstationBean;
    }

    @ApiOperation( value = "Returns processes list", response = ProcessDTO.class, responseContainer = "List")
    @GET
    @UnitOfWork
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getProcesses(){
        List<Process> processes = processBean.getAll();

        for (Process p : processes) {
            p.setNumberOfActivities(processBean.getNumberOfActivities(p));
            p.setNumberOfCases(processBean.getNumberOfCases(p));
        }

        return Response.status(Response.Status.OK).entity(processBean.toDTOsList(processes)).build();
    }

    @ApiOperation( value = "Get subprocess of process", response = ProcessDTO.class)
    @GET
    @UnitOfWork
    @Path( "{processId}/subprocess" )
    @RolesAllowed({"Operador","Gestor","Administrador"}) //????
    public Response getSubProcess( @PathParam("processId") long processId ) throws Exception {

            Process subProcess = processBean.getSubProcess(processId);
            if(subProcess == null){
                return Response.status(Response.Status.NOT_FOUND).entity("Processo '" + processId + "' não tem subprocesso associado").build();
            }
            return Response.status(Response.Status.OK).entity(processBean.toDTO(subProcess)).build();
    }

    @ApiOperation( value = "Get activities of a process", response = String.class, responseContainer = "List")
    @GET
    @UnitOfWork
    @Path("{processId}/activities")
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getActivitiesByProcessID( @PathParam("processId") long processId )
    {
        try {
            List<Activity> activities = processBean.getActivitiesfromProcessId( processId );

            return Response.status(Response.Status.OK).entity(activityBean.toDTOsList(activities)).build();
        }
        catch( EntityDoesNotExistException ex )
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN ).entity( ex.getMessage() ).build();
        }
    }

    @ApiOperation( value = "Get workstations of a process", response = String.class, responseContainer = "List")
    @GET
    @UnitOfWork
    @Path("{processId}/workstations")
    @RolesAllowed({"Operador","Gestor","Administrador"}) //?????????
    public Response getWorkstationsByProcessID( @PathParam("processId") long processId )
    {
        try {
            List<Workstation> workstations = processBean.getWorkstationsfromProcessId( processId );

            return Response.status(Response.Status.OK)
                    .entity(workstationBean.toDTOsList(workstations))
                    .build();

        }
        catch( EntityDoesNotExistException ex )
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
    }

    @ApiOperation( value = "Get users of a process", response = String.class, responseContainer = "List")
    @GET
    @UnitOfWork
    @Path("{processId}/users")
    @RolesAllowed({"Operador","Gestor","Administrador"}) //?????
    public Response getUsersByProcessID( @PathParam("processId") long processId )
    {
        try {
            List<ActivityUserEntry> entries = processBean.getUsersFromProcessId( processId );

            List<User> users = new LinkedList<>();
            for (ActivityUserEntry e : entries) {
                if(!users.contains(e.getUser())){
                    users.add(e.getUser());
                }
            }

            return Response.status(Response.Status.OK).entity(userBean.toDTOsList(users)).build();
        }
        catch( EntityDoesNotExistException ex )
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
    }

    @ApiOperation( value = "Create a new process", response = ProcessDTO.class)
    @POST
    @UnitOfWork
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response createNewProcess(ProcessDTO processDTO) {
        try {
            List<Process> processes = processBean.findName(processDTO.getName()); //não se pode criar um processo cujo nome já esteja registado na BD
            if (processes.size() != 0) {
                return Response.status(Response.Status.CONFLICT).build();
            }

            Process newProcess = new Process(processDTO.getName(), processDTO.getDescription());

            processBean.create(newProcess);

            return Response.status(Response.Status.CREATED).entity(processBean.toDTO(newProcess)).build();

        } catch (EntityExistsException e) {
            return Response.status(Response.Status.CONFLICT) .type( MediaType.TEXT_PLAIN )
                    .entity( e.getMessage() ) .build();
        }
    }

}
