package com.project.blogapi.security.tokens;

import com.project.blogapi.security.TokenService;
import com.project.blogapi.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserTokenService implements TokenService {

    private final UsersRepository usersRepository;
    private final UserTokenRepository userTokenRepository;

    public UserTokenService(
            @Autowired UsersRepository usersRepository,
            @Autowired UserTokenRepository userTokenRepository
    ) {
        this.usersRepository = usersRepository;
        this.userTokenRepository = userTokenRepository;
    }

    @Override
    public String createAuthToken(String username) {
        var user = usersRepository.findByUsername(username);
        var token = userTokenRepository.save(UserTokenEntity.builder()
                .user(user)
                .expiresAt(new Date(System.currentTimeMillis() + 1000*60*60*24)) //1 day
                .build()
        );
        return token.getId().toString(); //UUID as string would be token
    }

    @Override
    public String getUsernameFromToken(String token) throws IllegalStateException {
        var savedToken = userTokenRepository.findById(UUID.fromString(token));
        if(savedToken.isPresent()){
            var user = savedToken.get().getUser();
            if(user!=null){
                return user.getUsername();
            }
        }
        // generate security exception
        return null;
    }
}
