package com.project.blogapi.comments;

import com.project.blogapi.articles.ArticleEntity;
import com.project.blogapi.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentsRepository extends JpaRepository<CommentEntity, UUID> {


    List<CommentEntity> findAllByArticle(ArticleEntity article);
    List<CommentEntity> findAllByAuthor(UserEntity user);
}