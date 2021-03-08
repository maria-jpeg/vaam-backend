package projeto.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.*;
import projeto.api.dtos.AuthDTO;
import projeto.api.dtos.entities.ProcessDTO;
import projeto.auth.JwtBean;
import projeto.controller.UserBean;
import projeto.controller.exceptions.MyException;
import projeto.core.User;
import projeto.core.extra.Jwt;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Logger;

@SwaggerDefinition(tags = { @Tag(name = "login", description = "Login operations") })
@Api(value="login")
@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginServ {

    private static final Logger log = Logger.getLogger(LoginServ.class.getName());

    private final UserBean userBean;
    private final JwtBean jwtBean;

    public LoginServ(UserBean userBean, JwtBean jwtBean) {
        this.userBean = userBean;
        this.jwtBean = jwtBean;
    }

    @ApiOperation( value = "Authenticate user", response = Jwt.class)
    @POST
    @Path("/token")
    @UnitOfWork
    public Response authenticateUser(AuthDTO authDTO) { //if credentials are valid, sends back a user token
        try {
            String username = authDTO.getUsername();
            String password = authDTO.getPassword();

            User utilizador = userBean.authenticate(username, password);

            if (utilizador != null) {

                if (utilizador.getUsername() != null) {
                    log.info("Generating JWT for user " + utilizador.getUsername());
                }
                String token = jwtBean.createJWT(utilizador.getUsername(), utilizador.getRole().getName());

                return Response.ok(new Jwt(token)).build();
            }
        } catch (MyException e) {
            log.info(e.getMessage());
        }

        return Response.status(Response.Status.UNAUTHORIZED).entity("Credenciais inválidas").build();
    }

    @ApiOperation( value = "Demonstrate claims")
    @GET
    @Path("/claims")
    @RolesAllowed({"Operador","Gestor","Administrador"})
    public Response demonstrateClaims(@Auth @HeaderParam("Authorization") String auth) { //if user token valid, sends back role, username, exp dates and issuer
        System.out.println(auth);
        if (auth != null && auth.startsWith("Bearer ")) { //aqui o auth é onde vem o token no formato Bearer [token], por isso temos que retirar a parte inicial "Bearer "
            String jwt = auth.substring(7);

            Claims claims = jwtBean.decodeJWT(jwt);

            return Response.ok(claims).type(MediaType.APPLICATION_JSON_TYPE).build();
            //Note: nimbusds converts token expiration time to milliseconds
        }
        return Response.status(Response.Status.NO_CONTENT).entity("Token inválido").build(); //no jwt means no claims to extract
    }


}
