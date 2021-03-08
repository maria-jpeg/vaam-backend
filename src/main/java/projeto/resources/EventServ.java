package projeto.resources;


import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import org.checkerframework.checker.units.qual.A;
import projeto.api.dtos.entities.EventDTO;
import projeto.controller.ActivityBean;
import projeto.controller.EventBean;
import projeto.controller.MouldBean;
import projeto.controller.PartBean;
import projeto.controller.ProcessBean;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.EntityExistsException;
import projeto.controller.exceptions.TransformToEntityException;
import projeto.core.Activity;
import projeto.core.Event;
import projeto.core.Mould;
import projeto.core.Part;
import projeto.core.Process;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@SwaggerDefinition(tags = { @Tag(name = "events", description = "Operations related to Events") })
@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ") })
@Api(value="events")
@Path("/events") // relative url web path of this controller
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class EventServ{

    private final EventBean eventBean;
    private final ActivityBean activityBean;
    private final ProcessBean processBean;
    private final MouldBean mouldBean;
    private final PartBean partBean;

    public EventServ(EventBean eventBean, ActivityBean activityBean, ProcessBean processBean, MouldBean mouldBean, PartBean partBean) {
        this.eventBean = eventBean;
        this.activityBean = activityBean;
        this.processBean = processBean;
        this.mouldBean = mouldBean;
        this.partBean = partBean;
    }

    @ApiOperation( value = "Create a new event", response = EventDTO.class)
    @POST
    @UnitOfWork
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response createNewEvent(EventDTO eventDTO){
        try {
            Activity activity = activityBean.find(eventDTO.getActivityId());
            Process process = processBean.find(eventDTO.getProcessId());
            Mould mould = mouldBean.find(eventDTO.getMouldCode());
            Part part = null;
            if(eventDTO.getPartCode() != null){
                 part = partBean.find(eventDTO.getPartCode());
            }

            Event newEvent = new Event( activity, process, mould, part, eventDTO.getStartDate(), eventDTO.getEndDate(), eventDTO.getIsEstimatedEnd());

            eventBean.create(newEvent);

            return Response.status(Response.Status.CREATED)
                    .entity(eventBean.toDTO(newEvent))
                    .build();

        } catch (EntityExistsException e) {
            return Response.status(Response.Status.CONFLICT) .type( MediaType.TEXT_PLAIN )
                    .entity( e.getMessage() ) .build();
        }

    }

    @ApiOperation( value = "Update an event by id", response = EventDTO.class)
    @PUT
    @UnitOfWork
    @Path("{id}")
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response updateEndDateEvent(@PathParam("id") int id, EventDTO eventDTO) {
        try {
            EventDTO updatedEvent = eventBean.update(eventDTO);

            return Response.status(Response.Status.CREATED)
                    .entity(updatedEvent)
                    .build();

        } catch (EntityDoesNotExistException e) {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( e.getMessage() ) .build();
        } catch (TransformToEntityException e) {
            return Response.status(Response.Status.CONFLICT) .type( MediaType.TEXT_PLAIN )
                    .entity( e.getMessage() ) .build();
        }
    }

}
