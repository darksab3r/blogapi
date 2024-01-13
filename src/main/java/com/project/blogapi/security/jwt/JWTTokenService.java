package com.project.blogapi.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.blogapi.security.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

import static java.lang.System.*;

@Service
public class JWTTokenService implements TokenService {

    private final Algorithm algorithm;
    private  final String SIGNING_KEY="sdfsdfsdfsdf";
    private final long TOKEN_EXPIRY_MILLIS = (1000 * 60 * 60 * 24);
    private final String ISSUER = "blog-api";
    public JWTTokenService() {
        System.out.println("SIGNING_KEY: " + SIGNING_KEY);
        this.algorithm = Algorithm.HMAC256(SIGNING_KEY);
    }

    @Override
    public String createAuthToken(String username) {
        String token = JWT.create()
                .withIssuer(ISSUER)
                .withIssuedAt(new java.util.Date())
                .withExpiresAt(new java.util.Date(System.currentTimeMillis() + TOKEN_EXPIRY_MILLIS))
                .withSubject(username)
                .sign(algorithm);
        return token;
    }

    @Override
    public String getUsernameFromToken(String token) throws IllegalStateException {
        var verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();

        var decodedToken = verifier.verify(token);
        return decodedToken.getSubject();
    }
}
