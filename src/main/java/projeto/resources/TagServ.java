package projeto.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import io.swagger.util.Json;
import projeto.api.dtos.entities.PartDTO;
import projeto.controller.TagBean;
import projeto.controller.exceptions.EntityDoesNotExistException;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@SwaggerDefinition(tags = { @Tag(name = "tags", description = "Operations related to Tags") })
@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ") })
@Api(value="tags")
@Path("/tags") // relative url web path of this controller
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class TagServ {

    private final TagBean tagBean;

    public TagServ(TagBean tagBean) {
        this.tagBean = tagBean;
    }

    @ApiOperation( value = "Get all tags", response = Json.class)
    @GET
    @UnitOfWork
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getAllTags() {

        List<projeto.core.Tag> tags  = tagBean.getAll();

        return Response.status(Response.Status.OK)
                .entity(tagBean.toDTOsList(tags))
                .build();
    }

    @ApiOperation( value = "Get available or user tags", response = Json.class)
    @GET
    @UnitOfWork
    @Path("/availableOrUser")
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getAvailableOrUserTags() {

        List<projeto.core.Tag> tags  = tagBean.getAvailableOrUserTags();

        return Response.status(Response.Status.OK)
                .entity(tagBean.toDTOsList(tags))
                .build();


    }




}
