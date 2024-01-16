package com.project.blogapi.users;

import com.project.blogapi.security.TokenService;
import com.project.blogapi.users.dto.CreateUserRequestDTO;
import com.project.blogapi.users.dto.LoginUserRequestDTO;
import com.project.blogapi.users.dto.UpdateUserRequestDTO;
import com.project.blogapi.users.dto.UserResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;
    private final ModelMapper modelMapper;
    private final TokenService tokenService;
    public UsersController(
            @Autowired UsersService usersService,
            @Autowired ModelMapper modelMapper,
            @Autowired TokenService tokenService
    ) {
        this.usersService = usersService;
        this.modelMapper = modelMapper;
        this.tokenService = tokenService;
    }
  

    @PostMapping("/signup")
    ResponseEntity<UserResponseDTO> signupUser(@RequestBody CreateUserRequestDTO createUserRequestDTO){

        var savedUser = usersService.createUser(
                createUserRequestDTO.getUsername(),
                createUserRequestDTO.getPassword(),
                createUserRequestDTO.getEmail()
        );
        var userResponse = modelMapper.map(savedUser, UserResponseDTO.class);
        userResponse.setToken(tokenService.createAuthToken(savedUser.getUsername()));
        return ResponseEntity.accepted().body(userResponse);
    }

    @PostMapping("/login")
    ResponseEntity<UserResponseDTO> loginUser(@RequestBody LoginUserRequestDTO loginUserRequestDTO){
        var loggedInUser = usersService.loginUser(
                loginUserRequestDTO.getUsername(),
                loginUserRequestDTO.getPassword()
        );

        var userResponse = modelMapper.map(loggedInUser, UserResponseDTO.class);
        userResponse.setToken(tokenService.createAuthToken(loggedInUser.getUsername()));
        return ResponseEntity.accepted().body(userResponse);
    }

    @PatchMapping("/{inComingUserName}") //update user info
    ResponseEntity<UserResponseDTO> updateUser(
            @AuthenticationPrincipal String username,
            @PathVariable String inComingUserName,
            @RequestBody UpdateUserRequestDTO updateUserRequestDTO
            ){
        usersService.verifyUser(inComingUserName,username,true);
        var updatedUser = usersService.updateUser(inComingUserName,updateUserRequestDTO);
        var userResponse = modelMapper.map(updatedUser,UserResponseDTO.class);
        return ResponseEntity.accepted().body(userResponse);
    }

    @PutMapping("/{inComingUserName}/follow")
    ResponseEntity<String> followUser(
            @AuthenticationPrincipal String followerUsername,
            @PathVariable String inComingUserName
    ){
        usersService.verifyUser(inComingUserName,followerUsername,false);
        usersService.followUser(inComingUserName,followerUsername);
        return ResponseEntity.accepted().body("You started following");
    }

    @DeleteMapping("/{inComingUserName}/unfollow")
    ResponseEntity<String> unFollowUser(
            @AuthenticationPrincipal String followerUsername,
            @PathVariable String inComingUserName
    ){
        usersService.verifyUser(inComingUserName,followerUsername,false);
        usersService.unFollowUser(inComingUserName,followerUsername);
        return ResponseEntity.accepted().body("You unfollowed the user");
    }

}
