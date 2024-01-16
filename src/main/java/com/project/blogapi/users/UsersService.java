package com.project.blogapi.users;

import com.project.blogapi.articles.ArticlesService;
import com.project.blogapi.users.dto.UpdateUserRequestDTO;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsersService {

    private  final  UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

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

    public UserEntity getUserByUsername(String username){
        return usersRepository.findByUsername(username);
    }

    public UserEntity loginUser(String username, String password){
        var savedUser = getUserByUsername(username);
        if(savedUser!=null){
            if(passwordEncoder.matches(password, savedUser.getPassword())){
                return savedUser;
            }
        }
        throw new IllegalArgumentException("Invalid username or password");
    }

    public boolean verifyUser(String inComingUserName, String username, boolean isUserSame) {
        try {
            if (isUserSame!=inComingUserName.equals(username)) {
                throw new IllegalAccessException("You do not have rights for this action");
            }
        } catch (IllegalAccessException e) {
            // Handle the exception if needed
            e.printStackTrace();  // or log the exception
            return false;  // or handle the failure in some way
        }
        return true;
    }

    public UserEntity updateUser(String username, UpdateUserRequestDTO updateUserRequestDTO){
        var user = getUserByUsername(username);
        if(updateUserRequestDTO.getPassword()!=null){
            user.setPassword(updateUserRequestDTO.getPassword());
        }
        if(updateUserRequestDTO.getUsername()!=null){
            user.setPassword(updateUserRequestDTO.getUsername());
        }
        if(updateUserRequestDTO.getEmail()!=null){
            user.setEmail(updateUserRequestDTO.getEmail());
        }
        if(updateUserRequestDTO.getBio()!=null){
            user.setBio(updateUserRequestDTO.getBio());
        }
        usersRepository.save(user);
        return  user;
    }

    public boolean followUser(String followeeUserName, String followerUserName){
        var followee = getUserByUsername(followeeUserName);
        var follower = getUserByUsername(followerUserName);

        if (follower.getFollowing().contains(followee)) {
            throw new AlreadyFollowingException(followeeUserName);
        }

        follower.getFollowing().add(followee);
        usersRepository.save(follower);

        followee.getFollowers().add(follower);
        usersRepository.save(followee);

        return true;
    }

    public boolean unFollowUser(String followeeUserName, String followerUserName){
        var followee = getUserByUsername(followeeUserName);
        var follower = getUserByUsername(followerUserName);

        if (!follower.getFollowing().contains(followee)) {
            throw new NotFollowingException(followeeUserName);
        }

        follower.getFollowing().remove(followee);
        usersRepository.save(follower);

        followee.getFollowers().remove(follower);
        usersRepository.save(followee);

        return true;
    }

    static  class  AlreadyFollowingException extends RuntimeException{
        public AlreadyFollowingException(String username){
            super("You already follow "+username);
        }
    }

    static  class  NotFollowingException extends RuntimeException{
        public NotFollowingException(String username){
            super("You do not follow "+username);
        }
    }

}
