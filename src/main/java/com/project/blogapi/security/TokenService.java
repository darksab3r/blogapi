package com.project.blogapi.security;

public interface TokenService {
    /*
        create a JWT or Service side token for given username
     */
    String createAuthToken(String username);
    String getUsernameFromToken(String token) throws IllegalStateException;

}
