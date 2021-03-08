package projeto.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import projeto.core.AuthUser;
import projeto.core.User;


import java.util.Date;
import java.util.Optional;

public class OAuthAuthenticator implements Authenticator<String, AuthUser> {

    @Override
    public Optional<AuthUser> authenticate(String token) throws AuthenticationException { //token que vem do bearer
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("secret".getBytes())
                    .parseClaimsJws(token).getBody();

            long nowMillis = System.currentTimeMillis();
            Date nowDate = new Date(nowMillis);

            if(claims.getExpiration().before(nowDate)){
                System.out.println("TOKEN HAS EXPIRED");
                return Optional.empty();
            }
            return Optional.of(new AuthUser((String)claims.get("sub"), (String)claims.get("role")));
        }
        catch(Exception e) {
            return Optional.empty();
        }

    }

}
