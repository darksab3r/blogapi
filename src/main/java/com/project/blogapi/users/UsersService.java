package com.project.blogapi.users;

import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsersService {

    private  final  UsersRepository usersRepository;
    private PasswordEncoder passwordEncoder;

    public UsersService(
            @Autowired UsersRepository usersRepository,
            @Autowired PasswordEncoder passwordEncoder
    ) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity createUser(String username, String password, String email){
        var savedUser = usersRepository.save(
                UserEntity.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .email(email)
                        .build()
        );
        //TODO:
//            a.validate username min/max length
//            c.validate email format
        return savedUser;
    }

    public UserEntity getUserById(UUID userId){
        return usersRepository.findById(userId).orElse(null);
    }

    public UserEntity loginUser(String username, String password){
        var savedUser = usersRepository.findByUsername(username);
        if(savedUser!=null){
            if(passwordEncoder.matches(password, savedUser.getPassword())){
                return savedUser;
            }
        }
        throw new IllegalArgumentException("Invalid username or password");
    }
}
