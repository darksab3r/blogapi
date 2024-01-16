package com.project.blogapi.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.blogapi.articles.ArticleEntity;
import com.project.blogapi.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity(name = "users")
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class UserEntity extends BaseEntity {
    @NonNull  //for java
    @Column(nullable = false, unique = true, length = 30) //nullable here is for the DB
    String username;
    @NonNull
    @Column(nullable = false, unique = true, length = 50)
    String email;
    @NonNull
    @Column(nullable = false)
    String password; //using hashing and will store hash

    @Column(nullable = true)
    String bio;

    @ManyToMany(targetEntity = UserEntity.class, mappedBy = "following")
    List<UserEntity> followers;

    @ManyToMany
    List<UserEntity> following;

    @ManyToMany(targetEntity = ArticleEntity.class, mappedBy = "likes")
    List<ArticleEntity> favouriteArticles;
}
