package com.project.blogapi.users;

import jakarta.persistence.GeneratedValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, UUID> {

    @Query
    UserEntity findByUsername(String username);
    @Query
    UserEntity findByEmail(String email);
    @Query
    Optional<UserEntity> findById(UUID id);

}
