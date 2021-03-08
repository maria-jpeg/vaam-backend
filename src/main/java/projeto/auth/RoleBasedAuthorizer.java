package projeto.auth;

import io.dropwizard.auth.Authorizer;
import projeto.core.AuthUser;
import projeto.core.User;

public class RoleBasedAuthorizer implements Authorizer<AuthUser> {
    @Override
    public boolean authorize(AuthUser authUser, String role) { //o authUser é aquele que vem do return do OAuthAuthenticator; este role é o que vem do @RolesAllowed (podem ser múltiplos tho, nesse caso verifica um de cada vez)

        return (authUser.getRole().equals(role));
    }
}
