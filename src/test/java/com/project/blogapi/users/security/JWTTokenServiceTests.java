package com.project.blogapi.users.security;

import com.project.blogapi.security.jwt.JWTTokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JWTTokenServiceTests {
    private final JWTTokenService jwtTokenService =  new JWTTokenService();


    @Test
    public void testCreateAuthToken() {
        String username = "arnavg111";
        String token = jwtTokenService.createAuthToken(username);
        System.out.println(token);
        Assertions.assertNotNull(token);
    }

    @Test
    public void testTokenVerification() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJibG9nLWFwaSIsImlhdCI6MTcwNDcyODM3MywiZXhwIjoxNzA0ODE0NzczLCJzdWIiOiJhcm5hdmcxMTEifQ.q53v7yJau0y37-Qi5eE8j_IZfxdQmADVM9i1OLdF94U";
        String username = jwtTokenService.getUsernameFromToken(token);
        Assertions.assertEquals("arnavg111", username);
    }
}
