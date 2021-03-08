package projeto.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import io.swagger.util.Json;
import projeto.api.dtos.entities.PartDTO;
import projeto.controller.PartBean;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.core.Part;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@SwaggerDefinition(tags = { @Tag(name = "parts", description = "Operations related to Parts") })
@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ") })
@Api(value="parts")
@Path("/parts") // relative url web path of this controller
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class PartServ {

    private final PartBean partBean;

    public PartServ(PartBean partBean) {
        this.partBean = partBean;
    }

    @ApiOperation( value = "Get all parts", response = Json.class)
    @GET
    @UnitOfWork
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getParts() {

        List<Part> parts = partBean.getAll();

        return Response.status(Response.Status.OK)
                .entity(partBean.toDTOsList(parts))
                .build();
    }

    @ApiOperation( value = "Update a part by part code (set rfid)", response = PartDTO.class)
    @PUT
    @UnitOfWork
    @Path("{partCode}/tag/{tagRfid}")
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response setTagRfid(@PathParam("partCode") String partCode,@PathParam("tagRfid") String tagRfid)  throws Exception{

        try {
            PartDTO updatedPart = partBean.setTagRfid(partCode,tagRfid);
            return Response.status(Response.Status.CREATED)
                    .entity(updatedPart)
                    .build();
        }
        catch ( EntityDoesNotExistException  existException)
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( existException.getMessage() ) .build();
        }

    }

}
