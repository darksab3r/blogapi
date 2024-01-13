package com.project.blogapi.users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UsersRepositoryTests {

    @Autowired private UsersRepository usersRepository;

    @Test
    public void createUser(){
        UserEntity userEntity = UserEntity.builder()
                .username("barty")
                .email("barty@xyz.com")
                .password("password")
                .build();

        var user = usersRepository.save(userEntity);
        Assertions.assertNotNull(user.getId());
    }
    @Test
    public void findByUsername(){
        UserEntity userEntity1 = new UserEntity(
                "johndoe",
                "abc@xyz.com",
                "password"
        );
        usersRepository.save(userEntity1);
        UserEntity userEntity2 = new UserEntity(
                "carmine",
                "don@carmine.com",
                "password"
        );
        usersRepository.save(userEntity2);

        var user1= usersRepository.findByUsername("carmine");
        var user2 = usersRepository.findByUsername("johndoe");

        Assertions.assertEquals("don@carmine.com",user1.email);
        Assertions.assertEquals("abc@xyz.com",user2.email);
    }
}
