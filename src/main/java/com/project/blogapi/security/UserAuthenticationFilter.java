package com.project.blogapi.security;

import com.project.blogapi.security.TokenService;
import com.project.blogapi.security.UserAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;

public class UserAuthenticationFilter extends AuthenticationFilter {
    public UserAuthenticationFilter(TokenService tokenService) {
        super(new UserAuthenticationManager(tokenService), new BearerTokenAuthenticationConverter());
        /*
        every time authentication succeeds, we want to set the Authentication object
        in the SecurityContext of Spring
        This context is available throughout the springboot application with benefit that
        we can access principle and other details of authentication object
         */
        this.setSuccessHandler((request, response, authentication) -> {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        });

    }
    /*
    Converts request into an authentication object
     */
    static class BearerTokenAuthenticationConverter implements AuthenticationConverter{

        @Override
        public Authentication convert(HttpServletRequest request) {
            if(request.getHeader("Authorization")!=null){
                String token = request
                        .getHeader("Authorization")
                        .replace("Bearer ","");

                return new UserAuthentication(token);
            }
            return null;
        }
    }

    static class UserAuthenticationManager implements AuthenticationManager{

        private final TokenService tokenService;

        UserAuthenticationManager(TokenService tokenService) {
            this.tokenService = tokenService;
        }

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {

            if(authentication instanceof UserAuthentication){
                var userAuthentication = (UserAuthentication) authentication;
                var username = tokenService.getUsernameFromToken(userAuthentication.getCredentials());
                if(username!=null){
                    userAuthentication.setUser(username);
                    return userAuthentication;
                }
            }
            return null;
        }

    }
}
