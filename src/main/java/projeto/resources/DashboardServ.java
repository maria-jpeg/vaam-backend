package projeto.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import projeto.algorithms_process_mining.inductive_miner.InductiveMiner;
import projeto.api.dtos.entities.DashboardDTO;
import projeto.api.dtos.workflow_network.WorkflowNetworkPathsAndDeviationsDTO;
import projeto.controller.DashboardBean;
import projeto.controller.MouldBean;
import projeto.controller.ProcessBean;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.EntityExistsException;
import projeto.core.Dashboard;
import projeto.core.Event;
import projeto.core.Mould;
import projeto.core.Process;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@SwaggerDefinition(tags = {@Tag(name = "dashboard", description = "Dashboard")})
@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ")})
@Api("dashboard")
@Path("dashboard")
public class DashboardServ {

    private final DashboardBean dashboardBean;
    private final ProcessBean processBean;
    private final MouldBean mouldBean;

    public DashboardServ(DashboardBean dashboardBean, ProcessBean processBean, MouldBean mouldBean) {
        this.dashboardBean = dashboardBean;
        this.processBean = processBean;
        this.mouldBean = mouldBean;
    }

    @ApiOperation(value = "Get data for dashboard", response = DashboardDTO.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Dashboard has no data")})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getDashboardData() {
        List<Dashboard> dashboards = dashboardBean.getAll();

        return Response.status(Response.Status.OK)
                .entity(dashboardBean.toDTOsList(dashboards))
                .build();

    }

    @ApiOperation(value = "Generate data for dashboard")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Dashboard has no data")})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path("generate")
    public Response getGenerateDashboard() throws EntityDoesNotExistException {
        dashboardBean.cleanTable();
        List<Process> processes = processBean.getAll();
        for (Process process : processes) {
            InductiveMiner algorithm = new InductiveMiner(0.5, 0.5, true);
            try {
                WorkflowNetworkPathsAndDeviationsDTO workflowNetwork = processBean.getWorkFlowNetworkFromProcess(process.getId(), (InductiveMiner) algorithm);
                Dashboard dashboard = new Dashboard(workflowNetwork.getNumDeviations(), "int", "Desvios", process);
                dashboardBean.create(dashboard);
                List<Mould> mouldList = process.getMoulds();
                List<Event> eventList = process.getEvents();
                long sumDuration = 0;
                for (Event event : eventList) {
                    sumDuration += event.getDuration();
                }
                medianProcess(sumDuration / mouldList.size(), process);

            } catch (EntityDoesNotExistException | EntityExistsException ex) {
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN)
                        .entity(ex.getMessage()).build();
            }
        }
        addNumberOfMoulds();
        return Response.status(Response.Status.OK)
                .build();

    }

    public void addNumberOfMoulds() {
        try {
            Dashboard dashboard = new Dashboard(mouldBean.numberOfMoulds(), "int", "Número Moulds",null);
            dashboardBean.create(dashboard);
        } catch (EntityExistsException e) {
            e.printStackTrace();
        }
    }

    public void medianProcess(long median, Process process) {
        try {
            Dashboard dashboard = new Dashboard((int) median, "millis", "Duração média para a criação de um molde em milissegundos", process);
            dashboardBean.create(dashboard);
        } catch (EntityExistsException e) {
            e.printStackTrace();
        }
    }

}
