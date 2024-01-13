package com.project.blogapi.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, UUID> {

    @Query
    UserEntity findByUsername(String username);
    @Query
    UserEntity findByEmail(String email);

}
