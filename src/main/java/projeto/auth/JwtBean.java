package projeto.auth;


import java.security.Key;

import javax.crypto.spec.SecretKeySpec;
import io.jsonwebtoken.*;

import java.util.Date;

public class JwtBean {

    private static final int TOKEN_VALIDITY = 2000;
    private static final String CLAIM_ROLES = "role";
    private static final String ISSUER = "quickstart-jwt-issuer";
    private static final String AUDIENCE = "jwt-audience";

    public String createJWT(final String subject, final String role) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        //byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(apiKey.getSecret());
        byte[] apiKeySecretBytes = "secret".getBytes();
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());


        long expMillis = nowMillis + TOKEN_VALIDITY*1000;
        Date expDate = new Date(expMillis);

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .claim(CLAIM_ROLES,role)
                .setExpiration(expDate)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(ISSUER)
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        /*if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }*/

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }



    public Claims decodeJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey("secret".getBytes())
                .parseClaimsJws(jwt).getBody();
        return claims;
    }


}

