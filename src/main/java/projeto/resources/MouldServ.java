package projeto.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import io.swagger.util.Json;
import projeto.api.dtos.compare.FilterInputWrapperDTO;
import projeto.api.dtos.entities.*;
import projeto.controller.ActivityBean;
import projeto.controller.EventBean;
import projeto.controller.MouldBean;
import projeto.controller.PartBean;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.core.Event;
import projeto.core.Mould;
import projeto.core.Part;
import projeto.core.Process;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@SwaggerDefinition(tags = { @Tag(name = "moulds", description = "Operations related to Moulds") })
@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ") })
@Api(value="moulds")
@Path("/moulds") // relative url web path of this controller
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class MouldServ {

    private final MouldBean mouldBean;
    private final PartBean partBean;
    private final ActivityBean activityBean;
    private final EventBean eventBean;

    public MouldServ(MouldBean mouldBean, PartBean partBean,ActivityBean activityBean, EventBean eventBean) {
        this.mouldBean = mouldBean;
        this.partBean = partBean;
        this.activityBean = activityBean;
        this.eventBean = eventBean;
    }

    @ApiOperation( value = "Get all moulds", response = Json.class)
    @GET
    @UnitOfWork
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getMoulds() {

        List<Mould> moulds  = mouldBean.getAll();

        return Response.status(Response.Status.OK)
                .entity(mouldBean.toDTOsList(moulds))
                .build();
    }

    @ApiOperation( value = "Get parts of mould")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path( "/{mouldCode}/parts" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getPartsByMouldCode( @PathParam("mouldCode") String mouldCode ){
        try {

            List<Part> parts = mouldBean.getPartsByMouldCode(mouldCode);

            return Response.status(Response.Status.OK)
                    .entity(partBean.toDTOsList(parts))
                    .build();
        }
        catch ( EntityDoesNotExistException ex)
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
    }

    @ApiOperation( value = "Get activities associated with mould ", response = ActivityDTO.class, responseContainer = "List")
    @GET
    @UnitOfWork
    @Path( "{mouldCode}/activities" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getActivitiesByMouldCode (@PathParam("mouldCode") String mouldCode)
    {
        try
        {
            return Response.status(Response.Status.OK)
                    .entity(activityBean.getActivitiesFromMouldCode(mouldCode))
                    .build();
        }
        catch (EntityDoesNotExistException ex)
        {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN)
                    .entity(ex.getMessage()).build();
        }
    }

    @ApiOperation( value = "Get events associated with a mould ", response = EventDTO.class, responseContainer = "List")
    @GET
    @UnitOfWork
    @Path( "{mouldCode}/events" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getEventsByMouldCode (@PathParam("mouldCode") String mouldCode)
    {
        try
        {
            return Response.status(Response.Status.OK)
                    .entity(eventBean.getEventsFromMouldCode(mouldCode))
                    .build();
        }
        catch (EntityDoesNotExistException ex)
        {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN)
                    .entity(ex.getMessage()).build();
        }
    }

    @ApiOperation( value = "Get events associated with a mould with users and workstations", response = EventDTO.class, responseContainer = "List")
    @GET
    @UnitOfWork
    @Path( "{mouldCode}/eventsUsersWorkstations" )
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getEventsByMouldCodeWithUsersAndWorkstations (@PathParam("mouldCode") String mouldCode) throws Exception {
        try
        {
            return Response.status(Response.Status.OK)
                    .entity(eventBean.getEventsByMouldCodeWithUsersAndWorkstations(mouldCode))
                    .build();
        }
        catch (EntityDoesNotExistException | IOException ex)
        {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN)
                    .entity(ex.getMessage()).build();
        }
    }
}
