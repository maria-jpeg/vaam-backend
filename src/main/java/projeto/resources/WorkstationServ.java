package projeto.resources;


import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import projeto.api.dtos.entities.ProcessDTO;
import projeto.api.dtos.entities.WorkstationDTO;
import projeto.controller.ActivityBean;
import projeto.controller.WorkstationBean;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.EntityExistsException;
import projeto.controller.exceptions.TransformToEntityException;
import projeto.core.Activity;
import projeto.core.Process;
import projeto.core.Workstation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@SwaggerDefinition(tags = { @Tag(name = "workstations", description = "Operations related to Workstations") })
@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ") })
@Api(value="workstations")
@Path("/workstations") // relative url web path of this controller
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class WorkstationServ{

    private final WorkstationBean workstationBean;
    private final ActivityBean activityBean;

    public WorkstationServ(WorkstationBean workstationBean, ActivityBean activityBean) {
        this.workstationBean = workstationBean;
        this.activityBean = activityBean;
    }

    @ApiOperation( value = "Returns workstations list", response = WorkstationDTO.class, responseContainer = "List")
    @GET
    @UnitOfWork
    public Response getWorkstations() {
        List<Workstation> workstations = workstationBean.getAll();
        return Response.status(Response.Status.OK).entity(workstationBean.toDTOsList(workstations)).build();
    }

    @ApiOperation( value = "Returns tagging workstations list", response = WorkstationDTO.class, responseContainer = "List")
    @GET
    @Path("isTagging")
    @UnitOfWork
    public Response getTaggingWorkstations() {
        List<Workstation> workstations = workstationBean.getTaggingWorkstations();
        return Response.status(Response.Status.OK).entity(workstationBean.toDTOsList(workstations)).build();
    }

    @ApiOperation( value = "Create a new workstation", response = WorkstationDTO.class)
    @POST
    @UnitOfWork
    public Response createNewWorkstation(WorkstationDTO workstationDTO) {
        try {
            List<Workstation> workstations = workstationBean.findName(workstationDTO.getName()); //não se pode criar uma workstation cujo nome já esteja registado na BD
            if(workstations.size() != 0 ){
                return Response.status(Response.Status.CONFLICT).build();
            }

            Activity activity = activityBean.findOrFail(workstationDTO.getActivityId());
            Workstation newWorkstation = new Workstation(workstationDTO.getName(), workstationDTO.getIsTagging(), workstationDTO.getIsEndWorkstation(), activity);

            workstationBean.create(newWorkstation);

            return Response.status(Response.Status.CREATED).entity(workstationBean.toDTO(newWorkstation)).build();

        } catch (EntityDoesNotExistException ex) {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN ).entity( ex.getMessage() ) .build();
        } catch (EntityExistsException e) {
            return Response.status(Response.Status.CONFLICT) .type( MediaType.TEXT_PLAIN ).entity( e.getMessage() ) .build();
        }
    }

    @ApiOperation( value = "Get workstation by id", response = WorkstationDTO.class)
    @GET
    @Path("{id}")
    @UnitOfWork
    public Response getById( @PathParam("id") long id)
    {
        try {
            Workstation entity = workstationBean.findOrFail( id );
            return Response.status(Response.Status.CREATED).entity( workstationBean.getDao().toDTO(entity) ) .build();
        }
        catch (EntityDoesNotExistException ex)
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }

    }

    @ApiOperation( value = "Update workstation", response = WorkstationDTO.class)
    @PUT
    @UnitOfWork
    public Response update( WorkstationDTO workstationDTO ){
        try {
            Activity activity = activityBean.findOrFail(workstationDTO.getActivityId());
            Workstation workstation = new Workstation(workstationDTO.getId(), workstationDTO.getName(), workstationDTO.getIsTagging(), workstationDTO.getIsEndWorkstation(), activity);

            workstationBean.update(workstation);

            return Response.status(Response.Status.CREATED).entity(workstationBean.toDTO(workstation)).build();

        } catch (EntityDoesNotExistException ex) {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
    }

    @ApiOperation( value = "Delete a workstation by id", response = WorkstationDTO.class)
    @DELETE
    @Path("{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") long id)
    {
        try
        {
            Workstation removed = workstationBean.remove(id);
            return Response.ok( workstationBean.getDao().toDTO( removed ) ).build();
        }
        catch (EntityDoesNotExistException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity( ex.getMessage() ) .build();
        }

    }

}
