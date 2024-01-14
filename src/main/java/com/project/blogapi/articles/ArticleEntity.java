package com.project.blogapi.articles;

import com.project.blogapi.common.BaseEntity;
import com.project.blogapi.users.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name="articles")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ArticleEntity extends BaseEntity {

    @Column(nullable = false, length = 150)
    String title; //how to invest
    @Column(nullable = false, length = 100)
    String slug; //how-to-invest
    @Column(length = 250)
    String subtitle;
    @Column(nullable = false, length = 3000)
    String body;

    //TODO: tags
    @ManyToOne
    UserEntity author;

    @ManyToMany(targetEntity = UserEntity.class, cascade = CascadeType.ALL)
    @JoinTable(name = "article_likes",
            joinColumns = @JoinColumn(name="article_id"),
            inverseJoinColumns = @JoinColumn(name="user_id")
    )
    List<UserEntity> likes;
}
