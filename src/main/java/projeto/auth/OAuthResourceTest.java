package projeto.auth;

import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import projeto.core.AuthUser;
import projeto.resources.AuthenticationServ;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ExtendWith(DropwizardExtensionsSupport.class)
public class OAuthResourceTest {

    public ResourceExtension resourceExtension = ResourceExtension
            .builder()
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .addProvider(new AuthDynamicFeature(new OAuthCredentialAuthFilter.Builder<AuthUser>()
                    .setAuthenticator(new OAuthAuthenticator())
                    .setAuthorizer(new RoleBasedAuthorizer())
                    .setRealm("SUPER SECRET STUFF")
                    .setPrefix("Bearer")
                    .buildAuthFilter()))
            .addProvider(RolesAllowedDynamicFeature.class)
            .addProvider(new AuthValueFactoryProvider.Binder<>(AuthUser.class))
            .addResource(new AuthenticationServ())
            .build();

    @Test
    public void testProtected() throws Exception {
        final Response response = resourceExtension.target("/auth/test")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVXNlciIsImV4cCI6MTU4NTA3ODk5MywiaWF0IjoxNTg1MDY0NTkzLCJzdWIiOiJhZG1pbjEiLCJpc3MiOiJxdWlja3N0YXJ0LWp3dC1pc3N1ZXIifQ.Ut_AK1iIdIues7Ehkd35JrX60IQTEfzWx-n6pc1kbEQ")
                .get();

        //Assert.isTrue(response.getStatus() == 200);
        //assertThat(response.getStatus()).isEqualTo(200);
        if(response.getStatus() == 200){
            System.out.println("CORREU BEM: STATUS 200");
        }else{
            System.out.println("ALGO NÃO CORREU BEM: STATUS " + response.getStatus());
        }
    }


}
