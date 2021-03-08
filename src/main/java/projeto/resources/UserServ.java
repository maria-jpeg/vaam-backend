package projeto.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import projeto.api.dtos.entities.PartDTO;
import projeto.api.dtos.entities.ProcessDTO;
import projeto.api.dtos.entities.UserDTO;
import projeto.api.dtos.resources.ResourcesActivityDTO;
import projeto.controller.ProcessBean;
import projeto.controller.RoleBean;
import projeto.controller.TagBean;
import projeto.controller.UserBean;
import projeto.controller.exceptions.EntityDoesNotExistException;
import projeto.controller.exceptions.EntityExistsException;
import projeto.controller.exceptions.MyException;
import projeto.core.Process;
import projeto.core.Role;
import projeto.core.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@SwaggerDefinition(tags = { @Tag(name = "users", description = "Operations related to Users") })
@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer ") })
@Api(value="users")
@Path("/users") // relative url web path of this controller
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class UserServ {

    private final UserBean userBean;
    private final RoleBean roleBean;
    private final TagBean tagBean;
    private final ProcessBean processBean;

    public UserServ(UserBean userBean, RoleBean roleBean,TagBean tagBean, ProcessBean processBean) {
        this.userBean = userBean;
        this.roleBean = roleBean;
        this.tagBean = tagBean;
        this.processBean = processBean;
    }

    @ApiOperation( value = "Create a new User", response = UserDTO.class)
    @POST
    @UnitOfWork
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response createNewUser(UserDTO userDTO) {
        try {
            User aux1 = userBean.findUserCreate(userDTO.getUsername());
            if (aux1 != null) {
                return Response.status(Response.Status.CONFLICT).build();
            }

            List<User> users = userBean.findEmail(userDTO.getEmail()); //não se pode criar um utilizador cujo email já esteja registado na BD
            if (users.size() != 0) {
                return Response.status(Response.Status.CONFLICT).build();
            }

            Role role = roleBean.findOrFail(userDTO.getRole());

            userBean.create(new User(userDTO.getUsername(), userDTO.getPassword(), role, userDTO.getName(), userDTO.getEmail()));
            User newUser = userBean.findByUsername(userDTO.getUsername());

            return Response.status(Response.Status.CREATED).entity(userBean.toDTO(newUser)).build();

        } catch (EntityDoesNotExistException e) {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( e.getMessage() ) .build();
        } catch (EntityExistsException e) {
            return Response.status(Response.Status.CONFLICT).type( MediaType.TEXT_PLAIN )
                    .entity( e.getMessage() ).build();
        }
    }

    @ApiOperation( value = "Get user roles", response = String.class, responseContainer = "List")
    @GET
    @UnitOfWork
    @Path("roles")
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getUserRoles() {

        try {
            List<String> userRoles = roleBean.getUserRoles();

            return Response.status(Response.Status.OK).entity(userRoles).build();
        } catch (MyException e) {
            return Response.status(Response.Status.NO_CONTENT).type( MediaType.TEXT_PLAIN )
                    .entity( e.getMessage() ).build();
        }
    }

    @ApiOperation( value = "Get user with certain tag", response = UserDTO.class)
    @GET
    @UnitOfWork
    @Path("/tag/{tagRfid}")
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response getUserByTag(@PathParam("tagRfid") String tagRfid) {
        try {
            UserDTO user = userBean.findByTag(tagRfid);

            return Response.status(Response.Status.OK)
                    .entity(user)
                    .build();
        } catch (EntityDoesNotExistException ex) {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
    }

    @ApiOperation( value = "Update a user by username (set rfid)", response = UserDTO.class)
    @PUT
    @UnitOfWork
    @Path("{username}/tag/{tagRfid}")
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response setUserTagRfid(@PathParam("username") String username,@PathParam("tagRfid") String tagRfid) {
        try {
            UserDTO updatedUser = userBean.setTagRfid(tagBean, username,tagRfid);
            return Response.status(Response.Status.OK).entity(updatedUser).build();
        } catch (EntityDoesNotExistException ex) {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        } catch (EntityExistsException e) {
            return Response.status(Response.Status.CONFLICT) .type( MediaType.TEXT_PLAIN )
                    .entity( e.getMessage() ) .build();
        }

    }

    @ApiOperation( value = "Remove tag from user by username", response = UserDTO.class)
    @PUT
    @UnitOfWork
    @Path("{username}/tag/remove")
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response removeTagFromUserByUsername(@PathParam("username") String username) {
        try {
            UserDTO updatedUser = userBean.removeTagFromUserByUsername(username);
            return Response.status(Response.Status.OK).entity(updatedUser).build();

        } catch (EntityDoesNotExistException ex) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN)
                    .entity(ex.getMessage()).build();
        }
    }

    @ApiOperation( value = "Remove tag from user by tag rfid", response = UserDTO.class)
    @PUT
    @UnitOfWork
    @Path("/tag/{tagRfid}/remove")
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response removeTagFromUserByTagRfid(@PathParam("tagRfid") String tagRfid) {
        try {
            UserDTO updatedUser = userBean.removeTagFromUserByTagRfid(tagRfid);
            return Response.status(Response.Status.OK)
                    .entity(updatedUser)
                    .build();

        } catch (EntityDoesNotExistException ex) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN)
                    .entity(ex.getMessage()).build();
        }

    }

    @ApiOperation( value = "Get user processes", response = ProcessDTO.class, responseContainer = "List")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @UnitOfWork
    @RolesAllowed({"Operador","Gestor","Administrador"})
    @Path("{username}/processes")
    public Response getUserProcesses( @PathParam("username") String username )
    {
        try {

            List<Process> processes = userBean.getUserProcesses( username );
            return Response.status(Response.Status.OK).entity(processBean.toDTOsList(processes)).build();
        }
        catch( EntityDoesNotExistException ex )
        {
            return Response.status(Response.Status.NOT_FOUND) .type( MediaType.TEXT_PLAIN )
                    .entity( ex.getMessage() ) .build();
        }
    }


}
