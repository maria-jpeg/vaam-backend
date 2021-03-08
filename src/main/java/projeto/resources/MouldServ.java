package projeto.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import io.swagger.util.Json;
import projeto.api.dtos.entities.MouldDTO;
import projeto.api.dtos.entities.PartDTO;
import projeto.api.dtos.entities.ProcessDTO;
import projeto.controller.MouldBean;
import projeto.controller.PartBean;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.core.Mould;
import projeto.core.Part;
import projeto.core.Process;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    public MouldServ(MouldBean mouldBean, PartBean partBean) {
        this.mouldBean = mouldBean;
        this.partBean = partBean;
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

}
