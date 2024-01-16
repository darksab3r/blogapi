package com.project.blogapi.comments;

import com.project.blogapi.articles.ArticleEntity;
import com.project.blogapi.common.BaseEntity;
import com.project.blogapi.users.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name="comments")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class CommentEntity extends BaseEntity {

    @Column(length = 300)
    String title;
    @Column(nullable = false, length = 1000)
    String body;

    @ManyToOne
    UserEntity author;

    @ManyToOne
    ArticleEntity article;
}
