package projeto.resources;


import io.dropwizard.auth.Auth;
import projeto.api.dtos.AuthDTO;
import projeto.core.AuthUser;
import projeto.core.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/auth")
public class AuthenticationServ
{

    public AuthenticationServ() {
    }


    @GET
    @Path("/test")
    @RolesAllowed({"Operador","Gestor"})
    public Response getSecretPlan(@Auth AuthUser authUser) {
        System.out.println("ENTROU getSecretPlan() :D");
        System.out.println(authUser.getUsername());
        System.out.println(authUser.getRole());
        return Response.ok("Teste 1 OK").type(MediaType.APPLICATION_JSON_TYPE).build();
    }


    @GET
    @Path("/test2")
    @RolesAllowed("Gestor")
    public Response getSecretPlan2(@Auth AuthDTO authDTO) {
        System.out.println("ENTROU ENTROU getSecretPlan2() :D");
        System.out.println(authDTO.getUsername());
        System.out.println(authDTO.getPassword());
        return Response.ok("Teste 2 OK").type(MediaType.APPLICATION_JSON_TYPE).build();
    }

 /*   private final UserBean userBean;

    public AuthenticationServ(UserBean userBean) {
        this.userBean = userBean;
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    @Path("login")
    @UnitOfWork
    public Response login( CredentialsDTO credentials )
    {
        if( true )
            return null;

        if( credentials == null || credentials.getUsername().isEmpty() || credentials.getPassword().isEmpty() )
            return Response.status( Response.Status.BAD_REQUEST ).build();

        User user = userBean.findByUsername( credentials.getUsername() );
        boolean validCredentials = user != null && user.getPassword().equals( Utils.hashPassword( credentials.getPassword() ) );
        if( !validCredentials )
            return Response.status( Response.Status.UNAUTHORIZED ).build();

        String token = APIAuthenticator.createToken( user );
        TokenDTO tokenDTO = new TokenDTO( token );
        return Response.ok( tokenDTO ).build();

    }

    @NoArgsConstructor @AllArgsConstructor
    @Getter @Setter
    class TokenDTO
    {
        String token;
    }

*/
}